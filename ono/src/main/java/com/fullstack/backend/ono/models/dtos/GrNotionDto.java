package com.fullstack.backend.ono.models.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrNotionDto {

    private UUID id;

    @NotEmpty
    private String concept;

    @NotEmpty
    private UUID grammarNotion;
    
}
