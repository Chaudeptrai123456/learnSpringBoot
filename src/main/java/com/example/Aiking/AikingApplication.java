package com.example.Aiking;

import com.cloudinary.Cloudinary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AikingApplication {
	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary = null;
		Map config = new HashMap();
		config.put("cloud_name", "tienanh");
		config.put("api_key", "513676653359691");
		config.put("api_secret", "TPdREjkSAiuIyXDl9xe-yGfPLlY");
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}


	public static void main(String[] args) {
		SpringApplication.run(AikingApplication.class, args);
	}

}
