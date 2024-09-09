package se.chau.microservices.core.review.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.api.core.review.ReviewService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.core.review.Persistence.ReviewEntity;
import se.chau.microservices.core.review.Persistence.ReviewRepository;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.ArrayList;
import java.util.List;
@RestController
public class ReviewServiceImp implements ReviewService {
    private ServiceUtil serviceUtil;

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImp.class);

    private   ReviewRepository repository;

    private    ReviewMapper mapper;
    @Autowired
    public ReviewServiceImp(ServiceUtil serviceUtil, ReviewRepository repository, ReviewMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToApiList(entityList);
        list.forEach(e ->
                e.setServiceAddress(serviceUtil.getServiceAddress())
        );

        LOG.debug("getReviews: response size: {}", list.size());
        return list;
    }

    @Override
    public Review createReview(Review body) {
        try {
            ReviewEntity entity = mapper.apiToEntity(body);
            ReviewEntity newEntity = repository.save(entity);
            LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
            return mapper.entityToApi(newEntity);
        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
        }
    }
}
