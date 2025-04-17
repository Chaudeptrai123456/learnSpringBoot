package se.chau.microservices.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class OptService {

    private static final Logger LOG = LoggerFactory.getLogger(OptService.class);

    private final StringRedisTemplate redisTemplate;
    private static final int EXPIRE_MINUTES = 2;

    public OptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999)); // Tạo OTP 6 chữ số
        redisTemplate.opsForValue().set(email, otp.toString(), EXPIRE_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email); // đúng key đã lưu
        return otp.equals(storedOtp);
    }

}
