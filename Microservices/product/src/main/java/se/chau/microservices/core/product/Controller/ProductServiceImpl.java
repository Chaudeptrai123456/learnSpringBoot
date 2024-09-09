package se.chau.microservices.core.product.Controller;

import com.mongodb.DuplicateKeyException;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.product.Persistence.ProductEntity;
import se.chau.microservices.core.product.Persistence.ProductRepository;
import se.chau.microservices.util.http.ServiceUtil;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private ProductRepository productRepository;

    private final ProductMapper productMapper;
    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil, ProductRepository productRepository, ProductMapper productMapper) {
        this.serviceUtil = serviceUtil;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        ProductEntity entity = productRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
        Product response = productMapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());
        return response;
    }

    @Override
    public Product createProduct(Product product) {
        try {
            ProductEntity entity = productMapper.apiToEntity(product);
            ProductEntity newEntity = productRepository.save(entity);
            return productMapper.entityToApi(newEntity);
        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + product.getProductId());
        }
    }

    @Override
    public void deleteProduct(int productId) {
        productRepository.findByProductId(productId).ifPresent(e ->  productRepository.delete(e));
    }
}
