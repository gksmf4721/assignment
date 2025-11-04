package com.posicube.assignment.common.dto;

import java.time.LocalDateTime;

public record ResponseDto<T>(
        String path,
        String method,
        LocalDateTime timestamp,
        Integer status,
        T data
) {

    // 정적 팩토리 메서드 (of)
    public static <T> ResponseDto<T> of(String path, String method, T data) {
        return new ResponseDto<>(
                path,
                method,
                LocalDateTime.now(),
                200,
                data
        );
    }

    public static <T> ResponseDto<T> of(T data) {
        return new ResponseDto<>(
                "",
                "",
                LocalDateTime.now(),
                200,
                data
        );
    }
}
