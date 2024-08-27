package ToDo.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addToBlacklist(String token, long expiration) {
        String key = "blacklist:" + token;
        boolean success = redisTemplate.opsForValue().setIfAbsent(key, "blacklisted", expiration, TimeUnit.MILLISECONDS);
        if (!success) {
            throw new IllegalStateException("토큰이 이미 블랙리스트에 있습니다.");
        }
    }

    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void removeFromBlacklist(String token) {
        String key = "blacklist:" + token;
        boolean success = redisTemplate.delete(key);
        if (!success) {
            throw new IllegalStateException("블랙리스트에서 토큰을 찾을 수 없습니다.");
        }
    }
}
