package se.chau.microservices.core.feature.Service;
import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.Feature.FeatureForSearchPro;
import se.chau.microservices.api.core.Feature.FeatureService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.core.feature.Persistence.FeatureEntity;
import se.chau.microservices.core.feature.Persistence.FeatureRepository;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class FeatureServiceImpl implements FeatureService {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureServiceImpl.class);
    private  final ServiceUtil serviceUtil;
    @Autowired
    ReactiveMongoOperations mongoTemplate;
    private final FeatureRepository featureRepository;
    private final FeatureMapper mapper;
    @Autowired
    public FeatureServiceImpl(ServiceUtil serviceUtil, FeatureRepository featureRepository, FeatureMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.featureRepository = featureRepository;
        this.mapper = mapper;
    }
    @Override
    public Flux<Feature> getFeatureOfProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        LOG.info("Will get feature for product with id={}", productId);
        return featureRepository.findByProductId(productId)
                .log(LOG.getName(), FINE)
                .map(mapper::entityToApi)
                .map(this::setServiceAddress);
    }
    @Override
    public Flux<Feature> getProductByFeature(FeatureForSearchPro featureList) {
        LOG.debug("feature server get product by features {}",featureList.getName());
        return featureRepository.findByNameAndDescription(featureList.getName(),featureList.getDescription())
                .log(LOG.getName(), FINE)
                .map(mapper::entityToApi)
                .map(this::setServiceAddress);
    }

    @Override
    public Mono<Feature> createFeatureForProduct(Feature body) {
        if (body.getFeatureId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getFeatureId());
        }
        FeatureEntity entity =   mapper.apiToEntity(body);
        LOG.debug("create feature for product " + body.getFeatureId());
        entity.setProductId(body.getProductId());
        return featureRepository.save(entity)
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException
                                ("Duplicate key, Product Id: " + body.getFeatureId()))
                .map(mapper::entityToApi);
    }
    private Feature setServiceAddress(Feature feature) {
        feature.setServiceAddress(serviceUtil.getServiceAddress());
        return feature;
    }
}
