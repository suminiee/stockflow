package com.stockflow.global.apiPayload.exception.handler;

import com.stockflow.global.apiPayload.CustomResponse;
import com.stockflow.global.apiPayload.code.BaseErrorCode;
import com.stockflow.global.apiPayload.code.GeneralErrorCode;
import com.stockflow.global.apiPayload.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice(basePackages = {
        "com.stockflow.test"
}) //restController로 들어온 에러 처리해주는 핸들러 역할
public class ExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<String>> customException(CustomException e) {
        BaseErrorCode code = e.getCode();
        log.error(Arrays.toString(e.getStackTrace()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CustomResponse.onFailure(code.getStatus(), code.getCode(), code.getMessage(), false, null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<String>> customException(Exception e) {
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;
        log.error(Arrays.toString(e.getStackTrace()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CustomResponse.onFailure(code.getStatus(), code.getCode(), code.getMessage(), false, null)
        );
    }
}
