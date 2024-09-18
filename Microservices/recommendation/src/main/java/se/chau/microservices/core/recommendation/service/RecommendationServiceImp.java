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
import se.chau.microservices.core.recommendation.Persistence.RecommendationEntity;
import se.chau.microservices.core.recommendation.Persistence.RecommendationRepository;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class RecommendationServiceImp implements RecommendationService {
    private final ServiceUtil serviceUtil;
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImp.class);

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImp(ServiceUtil serviceUtil, RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.serviceUtil = serviceUtil;
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
          }
          LOG.info("Will get recommendations for product with id={}", productId);
          return recommendationRepository.findByProductId(Integer.parseInt(String.valueOf(productId)))
            .log(LOG.getName(), FINE)
            .map(recommendationMapper::entityToApi)
            .map(this::setServiceAddress);
                
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

