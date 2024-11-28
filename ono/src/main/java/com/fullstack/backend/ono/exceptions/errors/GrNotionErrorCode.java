package com.fullstack.backend.ono.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum GrNotionErrorCode implements ErrorCode {

    NOT_FOUND("NOTION_001", "Notion not found");

    private final String code;
    private final String description;

}
