package com.fullstack.backend.ono.exceptions;

import com.fullstack.backend.ono.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends ErrorCodeException {

    public BadRequestException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}