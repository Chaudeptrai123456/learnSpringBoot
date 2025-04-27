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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.api.core.order.ProductOrder;
import se.chau.microservices.api.core.product.Image;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.product.ProductUpdate;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.product.Configuration.ProductCacheService;
import se.chau.microservices.core.product.Persistence.ImageEntity;
import se.chau.microservices.core.product.Persistence.ImageRepository;
import se.chau.microservices.core.product.Persistence.ProductEntity;
import se.chau.microservices.core.product.Persistence.ProductRepository;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.logging.Level.FINE;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private static final int sizePage = 5;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final WebClient webClient;
    @Value("${url-discount}")
    private String urlDiscount;

    private final ProductCacheService productCacheService;

    @Autowired
    public ProductServiceImpl(ProductMapper mapper, ServiceUtil serviceUtil, ProductRepository repository, WebClient webClient, ProductCacheService productCacheService, ImageService imageService, ImageRepository imageRepository, ImageMapper imageMapper) {
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.webClient = webClient;
        this.productCacheService = productCacheService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
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
                .flatMap(this::setImagesForProduct)
                .map(this::setServiceAddress);
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
                                .log(LOG.getName(), FINE)  // Add logging at FINE level
                                .map(mapper::entityToApi)
                                .map(this::setServiceAddress)  // Add service address if needed
                                .flatMap(this::setImagesForProduct)
                                .doOnSuccess(result -> {
                                    // Log the successful product retrieval
                                    LOG.info("Quantity: {}", result.getQuantity());
                                    LOG.info("Saved to Redis: Product ID {}", result.getProductId());
                                    productCacheService.writeProductToRedis(result)
                                            .doOnError(error -> LOG.debug("Redis error: {}", error.getMessage()))
                                            .subscribe();  // Subscribe to persist the product to Redis asynchronously
                                })
                                .doOnError(error -> {
                                    // Log the error if something goes wron
                                    LOG.debug("Error during DB retrieval or Redis operations: {}", error.getMessage());
                                });

                    }
                });
    }
    private Mono<Product> setImagesForProduct(Product product) {
        // This method now returns a Mono, so it remains reactive and non-blocking
        return imageRepository.findByProductId(product.getProductId())
                .map(imageMapper::entityToApi)
                .collectList()
                .map(images -> {
                    product.setListImage(images);  // Set the images on the product
                    return product;  // Return the product with images
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
    public Mono<Double> sumCost(List<ProductOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return Mono.just(0.0);
        }

        return Flux.fromIterable(orders)
                .flatMap(order -> {
                    ProductUpdate productUpdate = new ProductUpdate();
                    productUpdate.setQuantity(-order.getQuantity());
                    productUpdate.setCost(0);

                    return updateProduct(productUpdate, order.getProductId())
                            .flatMap(updatedProduct -> getSaleOff(order.getProductId())
                                    .defaultIfEmpty(0.0)
                                    .map(saleOff -> updatedProduct.getCost() * order.getQuantity() * (1 - saleOff))
                            )
                            .onErrorResume(error -> {
                                LOG.error("Error updating or calculating saleOff for productId {}: {}", order.getProductId(), error.getMessage());
                                return Mono.just(0.0); // Nếu lỗi thì tính tiền sản phẩm đó = 0
                            });
                })
                .reduce(0.0, Double::sum)
                .doOnSuccess(total -> LOG.info("Total sum cost: {}", total))
                .doOnError(error -> LOG.error("Error in sumCost: {}", error.getMessage()));
    }

    private Mono<Double> getSaleOff(int productId){
        return webClient.get()
                .uri(urlDiscount + productId)
                .retrieve()
                .bodyToFlux(Discount.class)
                .log(LOG.getName(), FINE)
                .doOnError(error -> LOG.debug("ERROR get info from product service: " + error.getMessage()))
                .collectList()
                .map(discounts -> {
                    AtomicReference<Double> result = new AtomicReference<>(0.0);
                    discounts.forEach(index -> result.updateAndGet(v -> v + index.getValue()));
                    return result.get() / 100;
                });
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
    @PostMapping("/product/upload")
    public Mono<Image> upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam String productId, @RequestParam String name) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(UUID.randomUUID().toString());
        imageEntity.setProductId(Integer.parseInt(productId));
        imageEntity.setImageUrl(imageService.upload(multipartFile));
        return this.imageRepository.save(imageEntity).map(this.imageMapper::entityToApi);
    }
    @PostMapping(
            value = "/product/getImage"
    )
    Flux<Image> getImageList(@RequestParam String productId){
        LOG.info("get Image list of product " + productId);
        return this.imageRepository.findByProductId(Integer.parseInt(productId)).map(imageMapper::entityToApi);
//        return this.imageRepository.findAll();
    }
    private String getErrorMessage(WebClientResponseException ex) {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }
    private Boolean checkQuantity(int quantity, int productQuantity){
        return quantity > productQuantity;
    }
}