package com.stockflow.global.apiPayload.code;

import com.stockflow.global.apiPayload.CustomResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
    <T> CustomResponse<T> getResponse();
}
