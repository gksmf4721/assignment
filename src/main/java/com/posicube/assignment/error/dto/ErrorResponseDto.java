package com.posicube.assignment.error.dto;

import com.posicube.assignment.error.exception.ApiCommonException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String path,
        String method,
        int code,
        int status,
        Object data,
        String message,
        LocalDateTime timestamp
) {

    public ErrorResponseDto(String path, String method, int code, int status, Object data, String message) {
        this(path, method, code, status, data, message, LocalDateTime.now());
    }

    public static ResponseEntity<ErrorResponseDto> makeErrorResponse(
            ApiCommonException e,
            HttpServletRequest request
    ) {
        ErrorResponseDto dto = new ErrorResponseDto(
                request.getServletPath(),
                request.getMethod(),
                e.getCode().getCode(),
                e.getStatus().value(),
                e.getData(),
                e.getMessage()
        );
        return ResponseEntity
                .status(e.getStatus())
                .body(dto);
    }
}
