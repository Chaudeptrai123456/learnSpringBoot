package se.chau.microservices.core.product.Controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.core.product.Persistence.ProductEntity;
import se.chau.microservices.util.http.HttpErrorInfo;

@Mapper(componentModel = "spring")
public interface  ProductMapper {
    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
            @Mapping(target = "listImage",ignore = true)
    })
    Product entityToApi(ProductEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
    })
    ProductEntity apiToEntity(Product api);
    HttpErrorInfo readValue(String responseBodyAsString, Class<HttpErrorInfo> httpErrorInfoClass);

}