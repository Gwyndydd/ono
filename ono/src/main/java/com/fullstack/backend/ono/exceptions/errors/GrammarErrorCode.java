package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum GrammarErrorCode implements ErrorCode {

    ALREADY_EXISTS("GRAM_002", "Grammaire already exists"),
    NOT_FOUND("GRAM_001", "Grammaire not found");

    private final String code;
    private final String description;

}
