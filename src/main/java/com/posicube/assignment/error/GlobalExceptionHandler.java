package com.posicube.assignment.error;

import com.posicube.assignment.error.dto.ErrorResponseDto;
import com.posicube.assignment.error.exception.ApiCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiCommonException.class)
    public ResponseEntity<ErrorResponseDto> handleApiCommonException(
            ApiCommonException e,
            HttpServletRequest request
    ) {
        log.error("{} 발생: {}", e.getCode(), e.getMessage(), e);
        return ErrorResponseDto.makeErrorResponse(e, request);
    }
}
