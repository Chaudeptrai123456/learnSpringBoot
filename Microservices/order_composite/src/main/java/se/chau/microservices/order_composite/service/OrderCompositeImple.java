package se.chau.microservices.order_composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.api.core.order.OrderService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.io.IOException;

import static se.chau.microservices.api.event.Event.Type.SUMCOST;

@Service
public class OrderCompositeImple implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderCompositeImple.class);
    private final ObjectMapper mapper;
    private final StreamBridge streamBridge;
    private final Scheduler publishEventScheduler;
    private final ServiceUtil serviceUtil;
    @Autowired
    public OrderCompositeImple(ObjectMapper mapper, StreamBridge streamBridge, Scheduler publishEventScheduler, ServiceUtil serviceUtil) {
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.publishEventScheduler = publishEventScheduler;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Order> makingOrder(String token,Order body) {
        //check all product in productList
        return Mono.fromCallable(() -> {
            sendMessage("products-out-0",new Event(SUMCOST, "test",body));
            return new Order();
        }).subscribeOn(publishEventScheduler);

        // create order
    }

    @Override
    public Flux<Order> findOrderByUserId(int userId) {
        //get all id of products in list order serivce
        // get information from uri of product service

        return null;
    }

    @Override
    public Flux<Order> confirmOrder(int orderId) {
        return null;
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message<Event> message = MessageBuilder.withPayload(event).setHeader("partitionKey", event.getKey()).build();
        var a = streamBridge.send(bindingName, message);
        LOG.info("test send message from order-composite to product " , a);
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
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

}
