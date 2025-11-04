package com.posicube.assignment.domain.user.repository.impl;

import com.posicube.assignment.domain.user.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class UserCacheRepositoryImpl implements UserCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String findRemainingTokenByUser(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setRemainingTokenByUser(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, String.valueOf(value), ttl);
    }
}
