package com.fullstack.backend.ono.models.dtos;

import java.util.UUID;

import com.fullstack.backend.ono.models.constants.TypeVocabulary;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyDto {
    
    private UUID idVoca;

    @NotEmpty
    private String mot;

    private String definition;

    @NotEmpty
    private UUID idListe;

    @NotEmpty
    private TypeVocabulary type;
    
}
