package se.chau.microservices.Entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.core.User.User;
@Mapper(componentModel = "spring")
public interface UserMapper {
    User entityToApi(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "registrationDate", ignore = true),
            @Mapping(target = "authorities", ignore = true),
    })
    UserEntity apiToEntity(User api);

}
