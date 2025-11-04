package com.posicube.assignment.error.exception;

import com.posicube.assignment.error.GlobalErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiCommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private GlobalErrorCode code = GlobalErrorCode.UNKNOWN_INTERNAL_ERROR;
    private HttpStatus status = GlobalErrorCode.UNKNOWN_INTERNAL_ERROR.getStatus();
    private Object data;

    public ApiCommonException() {}

    public ApiCommonException(GlobalErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.status = code.getStatus();
    }

    // 메시지 생성자
    public ApiCommonException(String message) {
        super(message);
    }
}
