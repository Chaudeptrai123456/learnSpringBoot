package se.chau.microservices.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.User.User;

import java.time.Duration;
import java.util.NoSuchElementException;

@Service
public class RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    private final ReactiveRedisOperations<String, User> userCache;

    public RedisService(@Qualifier("redisOperationsUser") ReactiveRedisOperations<String, User> userCache) {
        this.userCache = userCache;
    }

    public void writeUser(User user,String otp){
        String key = "User" + user.getEmail() + " " + otp;

        if (user == null) {
            LOG.error("User is NULL before saving to Redis!");
              Mono.just(false);
        }

        LOG.info("✅ Preparing to save User to Redis: " + key + " | Value: " + user);

          userCache.opsForValue()
                .set(key, user, Duration.ofMinutes(2))
                .doOnSuccess(success -> LOG.info("User saved to Redis: " + key))
                .doOnError(error -> LOG.error("Failed to save user to Redis", error))
                .thenReturn(true).block();
    }
    public Mono<User> getUser(String email,String otp) {
        LOG.info("User which have email : " + email);
        return userCache.opsForValue().get("User"+email+" "+otp)
                .doOnNext(user -> LOG.info("User found: " + user))
                .doOnError(error -> LOG.error("Redis error", error))
                .switchIfEmpty(Mono.error(new NoSuchElementException("User not found in Redis")))
                .cache(Duration.ofMinutes(2));
    }

    public void deleteUser(String email) {
        userCache.opsForValue().delete(email);
    }
}
