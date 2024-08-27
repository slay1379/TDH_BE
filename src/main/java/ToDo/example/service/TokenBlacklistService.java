package ToDo.example.service;

import ToDo.example.authentication.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addToBlacklist(String token, long expiration) {
        String key = "blacklist:" + token;
        redisTemplate.opsForValue().set(key, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void removeFromBlacklist(String token) {
        String key = "blacklist:" + token;
        redisTemplate.delete(key);
    }
}
