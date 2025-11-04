package com.posicube.assignment.filter;

import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_REQUESTS = 30;
    private static final long WINDOW_MILLIS = 60 * 1000L; // 1분

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long now = Instant.now().toEpochMilli();

        String userId = request.getHeader("X-User-Id");
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = "rate_limit:" + userId;

        // 1분 이상 지난 요청 삭제
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, now - WINDOW_MILLIS);

        // 남은 요청 수 확인
        Long count = redisTemplate.opsForZSet().zCard(key);
        if (count != null && count >= MAX_REQUESTS) {
            throw new ApiCommonException(GlobalErrorCode.RATE_LIMIT_EXCEEDED);
        }

        // 현재 timestamp 추가
        redisTemplate.opsForZSet().add(key, String.valueOf(now), now);

        // TTL 갱신: 키가 너무 오래 남지 않도록 설정
        redisTemplate.expire(key, Duration.ofMinutes(2));

        filterChain.doFilter(request, response);
    }
}
