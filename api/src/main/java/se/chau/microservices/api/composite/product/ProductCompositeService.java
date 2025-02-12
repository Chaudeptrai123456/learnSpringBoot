package se.chau.microservices.api.composite.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.Feature.FeatureForSearchPro;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductFeature;
import se.chau.microservices.api.core.product.ProductUpdate;

import java.util.List;

@Tag(name="ProductComposite",description = "Restfull Api for product composite")
public interface ProductCompositeService {
    @Operation(
            summary = "${api.product-composite.create-composite-product.description}",
            description = "${api.product-composite.create-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value    = "/product-composite/create",
            consumes = "application/json")
    Mono<Void> createProduct(@RequestBody ProductAggregate body);

    @Operation(
            summary = "${api.product-composite.get-composite-product.description}",
            description = "${api.product-composite.get-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = "application/json")
    Mono<ProductAggregate> getProduct(@PathVariable int productId);

    @Operation(
            summary = "get product by list of features",
            description = "req is List of FeatureForSearchPro res is list of product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value = "/product-composite/product/feature",
            produces = "application/json")
    List<Product> getProductByFeature(@RequestBody List<FeatureForSearchPro> feature );

    @Operation(
            summary = "${api.product-composite.delete-composite-product.description}",
            description = "${api.product-composite.delete-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @DeleteMapping(value = "/product-composite/{productId}")
    void deleteProduct(@PathVariable int productId);
    @GetMapping(
            value="product-composite/page/{page}",
            produces = "application/json"
    )
    Flux<ProductFeature> getProductFeaturePage(@PathVariable int page) throws JsonProcessingException;
    @PatchMapping(
            value = "/product-composite/update/{productId}",
            produces = "application/json"
    )
    Mono<Product> updateProductComposite(@RequestBody ProductUpdate productUpdate,@PathVariable int productId);
}
