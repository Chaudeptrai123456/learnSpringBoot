package se.chau.microservices.core.feature.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.EventProcessingException;

import java.util.function.Consumer;
@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final FeatureServiceImpl featureService;

    @Autowired
    public MessageProcessorConfig(FeatureServiceImpl featureService) {
        this.featureService =   featureService;
    }

    @Bean
    public Consumer<Event<Integer, Feature>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Feature feature = event.getData();
                    LOG.info("Create feature with ID: {} ", feature.getFeatureId());
                    featureService.createFeatureForProduct(feature).block();
                    break;

                case DELETE:
                    int productId = event.getKey();
                    LOG.info("Delete feature with ProductID: {}", productId);
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
