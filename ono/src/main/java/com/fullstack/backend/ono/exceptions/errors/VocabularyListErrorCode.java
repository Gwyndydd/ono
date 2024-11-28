package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum VocabularyListErrorCode implements ErrorCode {
    
    ALREADY_EXISTS("VLIST_002", "List of vocabulary already exists"),
    NOT_FOUND("VLIST_001", "List of vocabulary not found");

    private final String code;
    private final String description;

}
