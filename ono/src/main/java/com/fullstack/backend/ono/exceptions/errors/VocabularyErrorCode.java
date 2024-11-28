package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum VocabularyErrorCode implements ErrorCode {

    ALREADY_EXISTS("VOCA_002", "Vocabulary already exists"),
    NOT_FOUND("VOCA_001", "Vocabulary not found");

    private final String code;
    private final String description;

}
