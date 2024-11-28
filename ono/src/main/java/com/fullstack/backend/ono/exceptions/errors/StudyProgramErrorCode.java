package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum StudyProgramErrorCode implements ErrorCode {

    ALREADY_EXISTS("SPROGRAM_002", "Study program already exists"),
    NOT_FOUND("SPROGRAM_001", "Study program not found");

    private final String code;
    private final String description;
}
