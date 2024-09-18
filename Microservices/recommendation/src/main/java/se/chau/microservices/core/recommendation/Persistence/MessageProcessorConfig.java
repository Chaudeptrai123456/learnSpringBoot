package se.chau.microservices.core.recommendation.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.recommandation.RecommendationService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.EventProcessingException;
import se.chau.microservices.core.recommendation.service.RecommendationServiceImp;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final RecommendationService recommendationService;

    @Autowired
    public MessageProcessorConfig(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Bean
    public Consumer<Event<Integer, Recommendation>> messageProcessor() {
        return event -> {

            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Recommendation recommendation = event.getData();
                    LOG.info("Create recommendation with ID: {}/{}", recommendation.getProductId(), recommendation.getRecommendationId());
                    recommendationService.createRecommendation(recommendation).block();
                    break;

                case DELETE:
                    int productId = event.getKey();
                    LOG.info("Delete recommendations with ProductID: {}", productId);
//                    recommendationService.deleteRecommendations(productId).block();
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }
}
