package se.chau.microservices.core.discount.Service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.order.DiscountEmail;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.api.discount.DiscountService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.discount.Persistence.DiscountEntity;
import se.chau.microservices.core.discount.Persistence.DiscountRepository;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.sql.Date;

import static java.util.logging.Level.FINE;

@RestController
public class DiscountServiceImple implements DiscountService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscountServiceImple.class);
    private final StreamBridge streamBridge;

    private final DiscountRepository repository;
    private final DiscountMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public DiscountServiceImple(StreamBridge streamBridge, DiscountRepository repository, DiscountMapper mapper, ServiceUtil serviceUtil) {
        this.streamBridge = streamBridge;
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }
    @Override
    public Mono<Discount> createDiscount(Discount discount) {
        LOG.info("test create discount " + discount.getStartDate());
        DiscountEntity discountEntity = mapper.apiToEntity(discount);
        LOG.info("test create discount " + discountEntity.getStartDate());
        discountEntity.setEndDate(Date.valueOf(discount.getEndDate()).toLocalDate());
        return  this.repository.save(discountEntity)
                        .log(LOG.getName(), FINE)
                        .doOnSuccess(r->{
                            sendMessage("orders-out-0",new Event(Event.Type.DISCOUNT_NOTIFICATION,1,new DiscountEmail("chau","nguyentienanh2001.dev@gmail.com")));
                        })
                        .map(mapper::entityToApi)
                        .onErrorMap(WebClientResponseException.class,
                                this::handleException
                        )
                ;
    }

    @Override
    public Flux<Discount> getDiscountOfPro(int productId) {
        LOG.info("get discount of product id: " + productId);
        return this.repository.findByProductId(productId)
                .log(LOG.getName(), FINE)
                .map(mapper::entityToApi)
                .map(this::setServiceAddress);
    }
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void createTTLIndex() {
        mongoTemplate.indexOps(DiscountEntity.class).ensureIndex(
                new Index().on("endDate", Sort.Direction.ASC).expire(0)
        );
    }
    // Method to delete expired discounts
//    @Scheduled(cron = "0 0 0 * * ?") // Cron expression for midnight every day
//    public void deleteExpiredDiscounts() {
//        LocalDate now = LocalDate.now();
//        List<DiscountEntity> expiredDiscounts = repository.findByEndDateBefore(now);
//
//        // Delete each expired discount
//        for (DiscountEntity discount : expiredDiscounts) {
//            repository.delete(discount);
//        }
//        LOG.info(expiredDiscounts.size() + " expired discounts deleted.");
//    }

    private Boolean checkDiscountActive(int id) {
        return false;
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
    private Discount setServiceAddress(Discount e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
    private String getErrorMessage(WebClientResponseException ex) {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }
    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message<Event> message = MessageBuilder.withPayload(event).setHeader("partitionKey", event.getKey()).build();
        streamBridge.send(bindingName, message);
    }
}
