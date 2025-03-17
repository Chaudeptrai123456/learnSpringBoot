package se.chau.microservices.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.User.User;

import java.time.Duration;

@Service
public class RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    private final ReactiveRedisOperations<String, User> userCache;

    public RedisService(@Qualifier("redisOperationsUser") ReactiveRedisOperations<String, User> userCache) {
        this.userCache = userCache;
    }

    public void writeUser(User user){
        userCache.opsForValue()
                .set(String.valueOf("User"+user.getEmail()), user, Duration.ofMinutes(2))  // Write to Redis
                .thenReturn(user).block();
    }
    public Mono<User> getUser(String email) {
        LOG.info("User which have email : " + email);
        return userCache.opsForValue().get("User"+email).cache(Duration.ofMinutes(2));
    }

    public void deleteUser(String email) {
        userCache.opsForValue().delete(email);
    }
}
