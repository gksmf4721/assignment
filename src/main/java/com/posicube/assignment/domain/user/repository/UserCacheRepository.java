package com.posicube.assignment.domain.user.repository;

import java.time.Duration;

public interface UserCacheRepository {

    String findRemainingTokenByUser(String key);

    void setRemainingTokenByUser(String key, String value, Duration ttl);
}
