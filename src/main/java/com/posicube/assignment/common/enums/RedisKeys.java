package com.posicube.assignment.common.enums;

import lombok.Getter;

@Getter
public enum RedisKeys {
    USER_TOKEN("user:%d:use_token");

    // 파라미터 없는 경우
    private final String key;

    RedisKeys(String key) {
        this.key = key;
    }

    // 파라미터가 있는 경우
    public String getKey(Object... params) {
        return String.format(key, params);
    }
}
