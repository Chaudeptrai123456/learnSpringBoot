package se.chau.microservices.order.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.order.Persistence.OrderEntity;
import se.chau.microservices.order.Persistence.OrderRepository;
import se.chau.microservices.order.Persistence.OrderStatus;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class OrderServiceImpl implements se.chau.microservices.api.core.order.OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final OrderMapper mapper;
    private final OrderRepository repository;
    @Autowired
    public OrderServiceImpl(ServiceUtil serviceUtil, OrderMapper mapper, OrderRepository repository) {
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Mono<Order> makingOrder(Order body) {
        if (body.getUserId() < 1) {
            throw new InvalidInputException("Invalid user: " + body.getUserId());
        }
        OrderEntity entity = mapper.apiToEntity(body);
        return repository.save(entity)
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException
                                ("Duplicate key, Product Id: " + body.getOrderId()))
                .map(mapper::entityToApi)
                ;
    }

    @Override
    public Flux<Order> findOrderByUserId(int userId) {
        if (userId< 1) {
            throw new InvalidInputException("Invalid user: " + userId);
        }
        return
                this.repository.findByUserId(userId)
                        .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + userId)))
                        .log(LOG.getName(), FINE)
                        .map(mapper::entityToApi)
                        .map(this::setServiceAddress)
                        .doOnError(error->{
                            LOG.debug("redis " + error.getMessage());
                        });
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
                                    .doOnError(error -> {
                                        LOG.debug("order " + error.getMessage());
                                    });
                        } else {
                            return Mono.error(new NotFoundException("No product found for productId: "));
                        }
                    });
    }

    private Order setServiceAddress(Order e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
}

