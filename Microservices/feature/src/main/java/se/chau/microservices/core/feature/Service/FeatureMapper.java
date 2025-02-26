package se.chau.microservices.core.feature.Service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.core.feature.Persistence.FeatureEntity;
@Mapper(componentModel = "spring")
public interface FeatureMapper {
    @Mappings(value ={
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "featureId", ignore = true)
    })
    FeatureEntity apiToEntity(Feature entity);

    @Mappings(value = {

            @Mapping(target = "serviceAddress", ignore = true)
    })
    Feature entityToApi(FeatureEntity api);

}
