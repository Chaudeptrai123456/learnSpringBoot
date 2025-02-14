package se.chau.microservices.order.Service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.chau.microservices.api.core.order.Order;
import se.chau.microservices.order.Persistence.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),

    })
    Order entityToApi(OrderEntity entity);
    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    OrderEntity apiToEntity(Order api);

}
