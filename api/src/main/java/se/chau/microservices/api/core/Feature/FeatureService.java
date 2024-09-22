package se.chau.microservices.api.core.Feature;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name="feature ",description = "Restfull Api for feature")
public interface FeatureService {
    @Operation(
            summary = "${api.product-composite.create-composite-product.description}",
            description = "${api.product-composite.create-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value="/feature/product",
            produces =  "application/json",
            consumes = "application/json"
    )
    Flux<Feature> getProductByFeature(@RequestBody FeatureForSearchPro feature ) throws HttpClientErrorException;
    @PostMapping(
            value="/feature/create",
            produces =  "application/json",
            consumes = "application/json"
    )
    Mono<Feature> createFeatureForProduct(@RequestBody Feature feature) throws HttpClientErrorException;

    @GetMapping(
            value = "/feature",
            produces = "application/json")
    Flux<Feature>getFeatureOfProduct(@RequestParam(name = "productId", defaultValue = "0") int productId);
}
