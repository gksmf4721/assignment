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

//    // 인터럽트 예외 핸들러
//    @ExceptionHandler(InterruptedCustomException.class)
//    public ResponseEntity<ErrorResponseDto> handleInterruptedException(
//            InterruptedCustomException e,
//            HttpServletRequest request
//    ) {
//        log.error("InterruptedException 발생: " + e.getMessage(), e);
//        ApiCommonException apiEx = new ApiCommonException(e.getCode());
//        return ErrorResponseDto.makeErrorResponse(apiEx, request);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
//            ResourceNotFoundException e,
//            HttpServletRequest request
//    ) {
//        log.error("ResourceNotFoundException 발생: " + e.getMessage(), e);
//        ApiCommonException apiEx = new ApiCommonException(e.getCode());
//        return ErrorResponseDto.makeErrorResponse(apiEx, request);
//    }
//
//    @ExceptionHandler(InvalidPermissionException.class)
//    public ResponseEntity<ErrorResponseDto> handleInvalidPermissionException(
//            InvalidPermissionException e,
//            HttpServletRequest request
//    ) {
//        log.error("InvalidPermissionException 발생: " + e.getMessage(), e);
//        ApiCommonException apiEx = new ApiCommonException(e.getCode());
//        return ErrorResponseDto.makeErrorResponse(apiEx, request);
//    }

    @ExceptionHandler(ApiCommonException.class)
    public ResponseEntity<ErrorResponseDto> handleApiCommonException(
            ApiCommonException e,
            HttpServletRequest request
    ) {
        log.error("{} 발생: {}", e.getCode(), e.getMessage(), e);
        return ErrorResponseDto.makeErrorResponse(e, request);
    }
}
