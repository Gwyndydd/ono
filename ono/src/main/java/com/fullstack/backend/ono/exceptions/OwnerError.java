package com.fullstack.backend.ono.exceptions;

import com.fullstack.backend.ono.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class OwnerError extends ErrorCodeException {

    public OwnerError(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public OwnerError(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}