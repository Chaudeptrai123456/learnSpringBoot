package se.chau.microservices.core.product_composite.service.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisService( @Qualifier("customRedisTemplate") RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T getSimple(Object key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void putSimple(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putSimpleWithTTL(Object key, Object value, Long ttl) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    public <T> T get(Object hashKey, Object key) {
        return (T) redisTemplate.opsForHash().get(hashKey, key);
    }

    public void put(Object hashKey, Object key, Object value, Long ttl) {
        redisTemplate.opsForHash().put(hashKey, key, value);
        redisTemplate.expire(hashKey, Duration.ofSeconds(ttl));
    }

//    public Long delete(Object hashKey, Object key) {
//        return redisTemplate.opsForHash().delete(hashKey, key);
//    }
    public Boolean delete(Object key) {
        return redisTemplate.delete(key);
    }
    public void putWithTTL(Object hashKey, Object key, Object value, long ttl) {
        redisTemplate.opsForHash().put(hashKey, key, value);
        redisTemplate.expire(hashKey, Duration.ofSeconds(ttl));
    }

    public void expire(Object hashKey, long ttl) {
        redisTemplate.expire(hashKey, Duration.ofSeconds(ttl));
    }
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private ObjectMapper objectMapper;
//    public <T> Mono<T> get(String key, Class<T> entityClass) {
//        return Mono.fromCallable(() -> {
//            try {
//                Object o = redisTemplate.opsForValue().get(key);
//                if (o == null) {
//                    return null;
//                }
//                ObjectMapper mapper = new ObjectMapper();
//                return mapper.readValue(o.toString(), entityClass);
//            } catch (Exception e) {
//                LOG.error("Exception", e);
//                return null;
//            }
//        });
//    }
//    public <T> Flux<T> getFlux(String key, Class<T> entityClass) {
//        return (Flux<T>) Mono.fromCallable(() -> {
//                    try {
//                        Object o = redisTemplate.opsForValue().get(key);
//                        if (o == null) {
//                            return null;
//                        }
//                        ObjectMapper mapper = new ObjectMapper();
//                        // Deserialize the JSON array into a List<T>
//                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, entityClass);
//                        return mapper.readValue(o.toString(), listType);
//                    } catch (Exception e) {
//                        LOG.error("Exception", e);
//                        return Collections.emptyList(); // Return an empty list in case of error
//                    }
//                })
//                .flatMapMany(Flux::fromIterable); // Convert List<T> to Flux<T>
//    }
//    public void set(String key, Object o, Long ttl) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonValue = objectMapper.writeValueAsString(o);
//            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            LOG.error("Exception ", e);
//        }
//    }
}
