package se.chau.microservices.core.product.Controller;

import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.api.core.order.ProductOrder;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.product.ProductUpdate;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.product.Configuration.ProductCacheService;
import se.chau.microservices.core.product.Persistence.ProductEntity;
import se.chau.microservices.core.product.Persistence.ProductRepository;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import static java.util.logging.Level.FINE;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private static final int sizePage = 5;

    private final WebClient webClient;
    @Value("${url-discount}")
    private String urlDiscount;

    private final ProductCacheService productCacheService;

    @Autowired
    public ProductServiceImpl(ProductMapper mapper, ServiceUtil serviceUtil, ProductRepository repository, WebClient webClient, ProductCacheService productCacheService) {
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.webClient = webClient;
        this.productCacheService = productCacheService;
    }
    @Override
    public Mono<Product> createProduct(Product body) {
        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }
        ProductEntity entity = mapper.apiToEntity(body);
        return repository.save(entity)
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException
                                ("Duplicate key, Product Id: " + body.getProductId()))
                .map(mapper::entityToApi)
                ;
    }
    @Override
    public Flux<Product> getProductPage(int page) {
        return this.repository.findAllBy(PageRequest.of(page,sizePage))
                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: ")))
                .log(LOG.getName(), FINE)
                .map(mapper::entityToApi)
                .map(this::setServiceAddress)
                ;
    }
    @Override
    public Mono<Product> getProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        LOG.info("Will get product info for id={}", productId);

        // Step 2: Check if the product exists in the cache
        return productCacheService.checkProductInCache(String.valueOf(productId))
                .flatMap(isInCache -> {
                    if (Boolean.TRUE.equals(isInCache)) {
                        // Product is in cache, return it
                        return productCacheService.getProductFromCache(String.valueOf(productId));
                    } else {
                        // Product is not in cache, fetch from database and cache it
                        return repository.findByProductId(productId)
                                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
                                .log(LOG.getName(), Level.FINE)  // Add logging at FINE level
                                .map(mapper::entityToApi)  // Map the entity to the API response model
                                .map(this::setServiceAddress)  // Add service address if needed
                                .doOnSuccess(result -> {
                                    // Log the successful product retrieval
                                    LOG.info("Quantity: {}", result.getQuantity());
                                    LOG.info("Saved to Redis: Product ID {}", result.getProductId());

                                    // Write the product to Redis
                                    productCacheService.writeProductToRedis(result)
                                            .doOnError(error -> LOG.debug("Redis error: {}", error.getMessage()))
                                            .subscribe();  // Subscribe to persist the product to Redis asynchronously
                                })
                                .doOnError(error -> {
                                    // Log the error if something goes wrong
                                    LOG.debug("Error during DB retrieval or Redis operations: {}", error.getMessage());
                                });
                    }
                });
    }
    @Override
    public Mono<Void> deleteProduct(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        return this.repository.findByProductId(productId).log(LOG.getName(), FINE).map(this.repository::delete).flatMap(e -> e);
    }
    @Override
    public Mono<Product> updateProduct(ProductUpdate product, int productId) throws HttpClientErrorException {
        return repository.findByProductId(productId)
                .flatMap(entity -> {
                    if (entity != null) {
                        LOG.info("quantity before update: " + entity.getQuantity());
                        if (entity.getQuantity() + product.getQuantity() >= 0) {
                            entity.setQuantity(entity.getQuantity() + product.getQuantity());
                        } else {
                            throw HttpClientErrorException.create(HttpStatusCode.valueOf(400),"Quantity is not enough", HttpHeaders.EMPTY,null,null);
                        }
                        LOG.info("cost of product " + entity.getProductId());
                        if (entity.getCost()!=0){
                            entity.setCost(entity.getCost() + product.getCost());
                        }
                        LOG.info("quantity after update: " + entity.getQuantity());
                        return repository.save(entity)
                                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
                                .map(mapper::entityToApi)
                                .map(this::setServiceAddress)
                                .doOnSuccess(result -> {
                                    LOG.info("Saved to Redis: Product ID " + result.getProductId());
                                })
                                .doOnError(error -> {
                                    LOG.debug("redis " + error.getMessage());
                                });
                    } else {
                        return Mono.error(new NotFoundException("No product found for productId: " + productId));
                    }
                });
    }
    private Product setServiceAddress(Product e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
    @Override
    public Mono<Double> sumCost(List<ProductOrder> list) throws HttpClientErrorException{
        try {
            LOG.info("call from order service");
            return Mono.fromCallable(()->{
                return list.stream()
                        .map(index -> {
                            LOG.info("test update product " + index.getProductId());
                            ProductUpdate productUpdate = new ProductUpdate();
                            productUpdate.setQuantity(-index.getQuantity());
                            productUpdate.setCost(0);
                            Product product = this.updateProduct(productUpdate, index.getProductId()).block();
                            return product != null ? product.getCost() * index.getQuantity()*getSaleOff(index.getProductId()) : 0.0;
                        })   // Map to the price of each product
                        .reduce(0.0, Double::sum);
            });
        } catch (Error error) {
            throw new InvalidInputException(error.getMessage());
        }

    }
    private Double getSaleOff(int productId){
        var discounts =  webClient.get().uri(urlDiscount+productId).retrieve()
                .bodyToFlux(Discount.class)
                .log(LOG.getName(),FINE)
                .doOnError(error -> LOG.debug("ERROR get info from product service: " + error.getMessage())).collectList().block();
        AtomicReference<Double> resutl = new AtomicReference<>(0.0);
        discounts.forEach(index->{
            resutl.updateAndGet(v -> v + index.getValue());
        });
        return resutl.get()/100;
    }
    private Order setServiceAddress(Order e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException wcre)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        switch (HttpStatus.resolve(wcre.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(wcre));

            case null:
                break;
            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
        return ex;
    }
    private String getErrorMessage(WebClientResponseException ex) {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }

    private Boolean checkQuantity(int quantity, int productQuantity){
        return quantity > productQuantity;
    }
}