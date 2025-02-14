package se.chau.microservices.core.product.Controller;

import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.product.ProductUpdate;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.product.Persistence.ProductEntity;
import se.chau.microservices.core.product.Persistence.ProductRepository;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private static final int sizePage = 5;
    @Autowired
    public ProductServiceImpl(ProductMapper mapper, ServiceUtil serviceUtil, ProductRepository repository) {
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
        this.repository = repository;
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
        return
                        this.repository.findByProductId(productId)
                                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
                                .log(LOG.getName(), FINE)
                                .map(mapper::entityToApi)
                                .map(this::setServiceAddress)
                                .doOnSuccess(result -> {
                                    LOG.info("quantity " + result.getQuantity());
                                    LOG.info("Saved to Redis: Product ID " + result.getProductId());
                                })
                                .doOnError(error->{
                                    LOG.debug("redis " + error.getMessage());
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
                        entity.setQuantity(entity.getQuantity() + product.getQuantity());
                        entity.setCost(entity.getCost() + product.getCost());
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
}