package se.chau.microservices.order.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.order.Persistence.OrderEntity;
import se.chau.microservices.order.Persistence.OrderRepository;
import se.chau.microservices.order.Persistence.OrderStatus;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class OrderServiceImpl implements se.chau.microservices.api.core.order.OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final StreamBridge streamBridge;

    private final ServiceUtil serviceUtil;
    private final OrderMapper mapper;
    private final OrderRepository repository;
    private final WebClient webClient;

    @Autowired
    public OrderServiceImpl(StreamBridge streamBridge, ServiceUtil serviceUtil, OrderMapper mapper, OrderRepository repository, WebClient webClient) {
        this.streamBridge = streamBridge;
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.repository = repository;
        this.webClient = webClient;
    }

    @Override
    public Mono<Order> makingOrder(Order body) {
        if (body.getUserId() < 1) {
            throw new InvalidInputException("Invalid user: " + body.getUserId());
        }
        LOG.debug("get product page");
        String url = "http://localhost:8081/product/sumcost";
        var cost =  webClient.post().uri(url).bodyValue(body.getProducts()).retrieve()
                .bodyToMono(Double.class)
                .log(LOG.getName(),FINE)
                .onErrorMap(WebClientResponseException.class,
                        this::handleException
                    )
                .doOnError(error -> LOG.debug("ERROR get info from product service: " + error.getMessage()));
        body.setCost(cost.block());
        OrderEntity entity = mapper.apiToEntity(body);
        return repository.save(entity).doOnSuccess(result -> {
                    LOG.info("test " + result.getUserId());
                })
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        WebClientResponseException.class,
                        this::handleException)
                .map(mapper::entityToApi)
                ;
    }
    @Override
    public Flux<Order> findOrderByUserId(int userId) {
        if (userId < 1) {
            throw new InvalidInputException("Invalid user: " + userId);
        }
        return
                this.repository.findByUserId(userId)
                        .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + userId)))
                        .log(LOG.getName(), FINE)
                        .map(mapper::entityToApi)
                        .map(this::setServiceAddress)
                        .onErrorMap(WebClientResponseException.class,
                                this::handleException
                        );
    }

    @Override
    public Flux<Order> confirmOrder(int orderId) {
        return repository.findByOrderId(orderId)
                .flatMap(entity -> {
                    if (entity != null) {
                        entity.setStatus(OrderStatus.DONE);
                        return repository.save(entity)
                                .switchIfEmpty(Mono.error(new NotFoundException("Fail")))
                                .map(mapper::entityToApi)
                                .map(this::setServiceAddress)
                                .doOnSuccess(result -> {
                                    LOG.info("Finish order : order ID " + orderId);
                                })
                                .onErrorMap(WebClientResponseException.class,
                                        this::handleException
                                );
                    } else {
                        return Mono.error(new NotFoundException("No product found for productId: "));
                    }
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

    private String getErrorMessage(WebClientResponseException ex) {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }

}


