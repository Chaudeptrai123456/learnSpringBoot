package se.chau.microservices.core.product.Controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.core.product.Image;
import se.chau.microservices.core.product.Persistence.ImageEntity;
import se.chau.microservices.util.http.HttpErrorInfo;
@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mappings({
    })
    Image entityToApi(ImageEntity entity);

    @Mappings({
            @Mapping(target = "id",ignore = true),
    })
    ImageEntity apiToEntity(Image api);
    HttpErrorInfo readValue(String responseBodyAsString, Class<HttpErrorInfo> httpErrorInfoClass);
}
