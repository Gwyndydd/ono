package com.fullstack.backend.ono.exceptions;

import com.fullstack.backend.ono.exceptions.errors.ErrorCode;

public abstract class ErrorCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ErrorCodeException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args), cause);
        this.errorCode = errorCode;
    }

    protected ErrorCodeException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}