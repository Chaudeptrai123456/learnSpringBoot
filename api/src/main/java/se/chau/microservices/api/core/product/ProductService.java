package se.chau.microservices.api.core.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    @Operation(
            summary = "${api.product-composite.get-product-composite.description}",
            description = "${api.product-composite.get-product-composite.notes}"
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "${api.responseCodes.ok.description}"),
                    @ApiResponse(responseCode = "400",description = "${api.responseCodes.badRequest.description}"),
                    @ApiResponse(responseCode = "404",description = "${api.responseCodes.notFound.description}"),
                    @ApiResponse(responseCode = "500",description = "${api.responseCodes.unprocessableEntity.description}")
            }
    )
    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json")
    Mono<Product> getProduct(@PathVariable int productId);
    @Operation(
            summary = "${api.product-composite.create-composite-product.description}",
            description = "${api.product-composite.create-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value="/product/create",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Product> createProduct(@RequestBody Product product) throws HttpClientErrorException;
    @GetMapping(
            value="product/page/{page}",
            produces = "application/json"
    )
    Flux<Product> getProductPage(@PathVariable int page);
    @DeleteMapping(
            value="/product/delete/{productId}"
    )
    Mono<Void> deleteProduct(@PathVariable int productId);
}
