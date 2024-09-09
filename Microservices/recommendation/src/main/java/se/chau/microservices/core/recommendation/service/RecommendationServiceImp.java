package se.chau.microservices.core.recommendation.service;


import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.recommandation.RecommendationService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.recommendation.Persistence.RecommendationEntity;
import se.chau.microservices.core.recommendation.Persistence.RecommendationRepository;
import se.chau.microservices.util.http.ServiceUtil;

import java.util.ArrayList;
import java.util.List;
@RestController
public class RecommendationServiceImp implements RecommendationService {
    private ServiceUtil serviceUtil;
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImp.class);

    private RecommendationRepository recommendationRepository;
    private RecommendationMapper recommendationMapper;

    public RecommendationServiceImp(ServiceUtil serviceUtil, RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.serviceUtil = serviceUtil;
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        List<RecommendationEntity> entityList = recommendationRepository.findByProductId(productId);
        List<Recommendation> list = recommendationMapper.entityListToApiList(entityList);
        list.forEach(e ->
                {
                    e.setServiceAddress(serviceUtil.getServiceAddress());
                }
        );
        LOG.debug("getRecommendations: response size: {}", list.size());
        return list;
    }

    @Override
    public Recommendation createRecommendation(Recommendation recommendation,int productId) {
        try {
            RecommendationEntity recommendationEntity = recommendationMapper.apiToEntity(recommendation);
            recommendationEntity.setProductId(productId);
            recommendationEntity.setContent(recommendation.getContent());
            RecommendationEntity newRecommendation = recommendationRepository.save(recommendationEntity);
            LOG.debug("createRecommendation : create new recommendation");

            return recommendationMapper.entityToApi(newRecommendation);
        } catch (DuplicateKeyException ex) {
            throw new InvalidInputException("Duplicate key, Productid :" + recommendation.getRecommendationId());
        }
    }
}

