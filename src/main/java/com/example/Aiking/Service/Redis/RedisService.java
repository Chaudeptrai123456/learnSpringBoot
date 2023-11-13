package com.example.Aiking.Service.Redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    public static final String KEY = "redis-readasdsadsa";
    private RedisTemplate<String,String> redisTemplate;
    private HashOperations hashOperations;


}
