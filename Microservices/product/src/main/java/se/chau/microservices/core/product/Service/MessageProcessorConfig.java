package se.chau.microservices.core.product.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.product.ProductUpdate;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.EventProcessingException;
import se.chau.microservices.core.product.Controller.ProductServiceImpl;

import java.util.function.Consumer;
 

@Configuration
public class MessageProcessorConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

  private final ProductServiceImpl productService;

  @Autowired
  public MessageProcessorConfig(ProductService productService) {
    this.productService = (ProductServiceImpl) productService;
  }

  @Bean
  public Consumer<Event<Integer, Product>> messageProcessor() {
    return event -> {
      LOG.info("Process message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {

        case CREATE:
          Product product = event.getData();
          LOG.info("Create product with ID: {} ", product.getProductId());
          productService.createProduct(product).block();
          break;

        case DELETE:
          int productId = event.getKey();
          LOG.info("Delete product with ProductID: {}", productId);
          productService.deleteProduct(productId).block();
          break;
//        case UPDATE:
//          ProductUpdate productUpdate = event.getData();
//          productService.updateProduct().block();
//          break;
        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          LOG.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }
      LOG.info("Message processing done!");
    };
  }
  @Bean
  public Consumer<Event<Integer, ProductUpdate>> messageProcessor1() {
    return event -> {
      LOG.info("Process message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {

        case UPDATE:
          ProductUpdate product = event.getData();
          int id = event.getKey();
          LOG.info("Create product with ID: {} ", id);
          productService.updateProduct(product,id).block();
          break;
        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          LOG.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }
      LOG.info("Message processing done!");
    };
  }
}