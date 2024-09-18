package se.chau.microservices.core.review.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.api.core.review.ReviewService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.core.review.Persistence.ReviewEntity;
import se.chau.microservices.core.review.Persistence.ReviewRepository;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
public class ReviewServiceImp implements ReviewService {
    private final ServiceUtil serviceUtil;

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImp.class);

    private final ReviewRepository repository;
    private final Scheduler jdbcScheduler;

    private final ReviewMapper mapper;
    @Autowired
    public ReviewServiceImp(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler,ServiceUtil serviceUtil, ReviewRepository repository, ReviewMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.jdbcScheduler = jdbcScheduler;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Flux<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return Mono.fromCallable(() -> internalGetReviews(productId))
                .flatMapMany(Flux::fromIterable)
                .log(LOG.getName(), FINE)
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Review> createReview(Review body) {
        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }
        return Mono.fromCallable(() -> internalCreateReview(body))
                .subscribeOn(jdbcScheduler);
    }
    private Review internalCreateReview(Review body) {
        try {
            ReviewEntity entity = mapper.apiToEntity(body);
            ReviewEntity newEntity = repository.save(entity);

            LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
            return mapper.entityToApi(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
        }
    }
    private Review setServiceAddress(Review e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }

    private List<Review> internalGetReviews(int productId) {

        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToApiList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("Response size: {}", list.size());

        return list;
    }
}
