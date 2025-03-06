package se.chau.microservices.core.recommendation.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import se.chau.microservices.api.core.recommandation.Recommendation;

@Configuration
public class RedisConfiguration {
    @Bean
    ReactiveRedisOperations<String, Recommendation> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Recommendation> serializer = new Jackson2JsonRedisSerializer<>(Recommendation.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Recommendation> builder =  RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Recommendation> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}