package com.fullstack.backend.ono.exceptions;

import com.fullstack.backend.ono.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends ErrorCodeException {

    public ConflictException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}