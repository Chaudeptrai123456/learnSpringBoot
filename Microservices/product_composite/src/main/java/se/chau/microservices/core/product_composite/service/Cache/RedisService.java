package se.chau.microservices.core.product_composite.service.Cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    public <T> Mono<T> get(String key, Class<T> entityClass) {
        return Mono.fromCallable(() -> {
            try {
                Object o = redisTemplate.opsForValue().get(key);
                if (o == null) {
                    return null;
                }
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(o.toString(), entityClass);
            } catch (Exception e) {
                LOG.error("Exception", e);
                return null;
            }
        });
    }
    public <T> Flux<T> getFlux(String key, Class<T> entityClass) {
        return (Flux<T>) Mono.fromCallable(() -> {
                    try {
                        Object o = redisTemplate.opsForValue().get(key);
                        if (o == null) {
                            return null;
                        }
                        ObjectMapper mapper = new ObjectMapper();
                        // Deserialize the JSON array into a List<T>
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, entityClass);
                        return mapper.readValue(o.toString(), listType);
                    } catch (Exception e) {
                        LOG.error("Exception", e);
                        return Collections.emptyList(); // Return an empty list in case of error
                    }
                })
                .flatMapMany(Flux::fromIterable); // Convert List<T> to Flux<T>
    }
    public void set(String key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
    }

}
