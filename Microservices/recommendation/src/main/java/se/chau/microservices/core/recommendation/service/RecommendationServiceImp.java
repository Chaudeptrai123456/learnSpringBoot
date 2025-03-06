package se.chau.microservices.core.recommendation.service;


import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.recommandation.RecommendationService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.recommendation.Config.RecommendationCacheService;
import se.chau.microservices.core.recommendation.Persistence.RecommendationEntity;
import se.chau.microservices.core.recommendation.Persistence.RecommendationRepository;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class RecommendationServiceImp implements RecommendationService {
    private final ServiceUtil serviceUtil;
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImp.class);
    private final RecommendationMapper mapper;

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    private final RecommendationCacheService recommendationCacheService;

    public RecommendationServiceImp(ServiceUtil serviceUtil, RecommendationMapper mapper, RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper, RecommendationCacheService recommendationCacheService) {
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
        this.recommendationCacheService = recommendationCacheService;
    }

//    @Override
//    public Flux<Recommendation> getRecommendations(int productId) {
//
//        if (productId < 1) {
//            throw new InvalidInputException("Invalid productId: " + productId);
//        }
//        LOG.info("Will get recommendations for product with id={}", productId);
//        return recommendationRepository.findByProductId(Integer.parseInt(String.valueOf(productId)))
//                .log(LOG.getName(), FINE)
//                .map(recommendationMapper::entityToApi)
//                .map(this::setServiceAddress);
//    }
@Override
public Flux<Recommendation> getRecommendations(int productId) {
    if (productId < 1) {
        throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.info("Will get recommendations for product with id={}", productId);

    // Step 1: Check if the recommendations exist in the cache
    return recommendationCacheService.checkRecommendationInCache(String.valueOf(productId))
            .flatMapMany(isInCache -> {
                if (Boolean.TRUE.equals(isInCache)) {
                    // Recommendations are in cache, return them
                    return recommendationCacheService.getRecommendationFromCache(String.valueOf(productId));
                } else {
                    // Recommendations are not in cache, fetch from database
                    return recommendationRepository.findByProductId(productId)
                            .switchIfEmpty(Flux.error(new NotFoundException("No recommendations found for productId: " + productId)))
                            .log(LOG.getName(), FINE)  // Add logging at FINE level
                            .map(recommendationMapper::entityToApi)
                            .map(this::setServiceAddress)
                            .doOnEach(recommendationSignal -> {
                                recommendationCacheService.writeRecommendationToRedis(recommendationSignal.get());
                            })
                            ;  // Add service address if needed
                }
            });
}

    @Override
    public Mono<Recommendation> createRecommendation(Recommendation recommendation) {
        if (recommendation.getRecommendationId() < 1) {
            throw new InvalidInputException("Invalid productId: " + recommendation.getRecommendationId());
        }
        LOG.debug("create recommendation id " + recommendation.getRecommendationId());
        RecommendationEntity entity = recommendationMapper.apiToEntity(recommendation);

        return recommendationRepository.save(entity)
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + recommendation.getProductId() + ", Recommendation Id:" + recommendation.getRecommendationId()))
                .map(recommendationMapper::entityToApi);
    }
    private Recommendation setServiceAddress(Recommendation e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
}

