package se.chau.microservices.core.discount.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.api.discount.DiscountService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.discount.Persistence.DiscountEntity;
import se.chau.microservices.core.discount.Persistence.DiscountRepository;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.UUID;

import static java.util.logging.Level.FINE;

@RestController
public class DiscountServiceImple implements DiscountService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscountServiceImple.class);

    private final DiscountRepository repository;
    private final DiscountMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public DiscountServiceImple(DiscountRepository repository, DiscountMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Discount> createDiscount(Discount discount) {
        DiscountEntity entity = new DiscountEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDiscountId(UUID.randomUUID().toString());
        entity.setValue(discount.getValue());
        entity.setStartDate(discount.getStart());
        entity.setEndDate(discount.getEnd());
        entity.setProductId(discount.getProductId());
        entity.setDescription(discount.getDescription());
        return  this.repository.save(entity)
                        .log(LOG.getName(), FINE)
                        .map(mapper::entityToApi)
                        .onErrorMap(WebClientResponseException.class,
                                this::handleException
                        )
                ;
    }

    @Override
    public Flux<Discount> getDiscountOfPro(int productId) {
        return this.repository.findByProductId(productId)
                .log(LOG.getName(), FINE)
                .map(mapper::entityToApi);
    }

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
    private Recommendation setServiceAddress(Recommendation e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
    private String getErrorMessage(WebClientResponseException ex) {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }
}
