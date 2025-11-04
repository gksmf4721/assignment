package com.posicube.assignment.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode {

    // 알 수 없는 에러 (공통)
    UNKNOWN_INTERNAL_ERROR(10000, "알 수 없는 에러.", HttpStatus.INTERNAL_SERVER_ERROR),
    CALL_ABORT(10001, "호출이 중단되었습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    NOT_HAVE_TOKEN(10002, "사용 가능한 토큰이 없습니다.", HttpStatus.CONFLICT),
    NOT_FOUND_USER(10003, "유저 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    RATE_LIMIT_EXCEEDED(10004, "1분당 요청 한도를 초과했습니다.", HttpStatus.TOO_MANY_REQUESTS),
    NOT_FOUND_ASSISTANT_MODEL(10005, "모델 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PLAN(10005, "플랜 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


    private int code;
    private String message;
    private HttpStatus status;

    GlobalErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
