package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum GrammarListErrorCode implements ErrorCode {
    
    ALREADY_EXISTS("GLIST_002", "List of grammar already exists"),
    NOT_FOUND("GLIST_001", "List of grammar not found");

    private final String code;
    private final String description;
}
