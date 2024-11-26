package com.fullstack.backend.ono.exceptions;

import com.fullstack.backend.ono.exceptions.errors.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends ErrorCodeException {

    public NotFoundException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
