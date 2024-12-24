package se.chau.microservices.core.product_composite.service.Cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Component
public class CustomRedisSerializer implements RedisSerializer<Object> {
    private final ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(CustomRedisSerializer.class);

    public CustomRedisSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
    }
    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return new byte[0];
        }
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            LOG.error("JsonProcessingException while compressing value to redis {}",
                    e.getMessage(), e);
        }
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if(bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return mapper.readValue(bytes, Object.class);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}
