package se.chau.microservices.core.discount.Service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.core.discount.Persistence.DiscountEntity;
import se.chau.microservices.util.http.HttpErrorInfo;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
    })
    Discount entityToApi(DiscountEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    DiscountEntity apiToEntity(Discount api);
    HttpErrorInfo readValue(String responseBodyAsString, Class<HttpErrorInfo> httpErrorInfoClass);
}
