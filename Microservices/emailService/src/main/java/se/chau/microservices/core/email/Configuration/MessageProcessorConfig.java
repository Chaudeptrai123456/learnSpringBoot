package se.chau.microservices.core.email.Configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.chau.microservices.api.core.order.Email;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.core.email.Service.EmailImplement;

import java.util.function.Consumer;


@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final EmailImplement emailImplement;

    @Autowired
    public MessageProcessorConfig(EmailImplement emailImplement) {
        this.emailImplement =  emailImplement;
    }

    @Bean
    public Consumer<Event<Integer, Email>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case MAKING_ORDER:
                    Email email = event.getData();
                    LOG.info("Send email : {} ",email.getEmail());
                    emailImplement.placeOrder(email);
                    break;

            }

            LOG.info("Message processing done!");

        };
    }
}