package com.fullstack.backend.ono.models.dtos;

import java.util.UUID;

import jakarta.validation.Valid;
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

    //@NotEmpty
    private String word;

    private String definition;

    private UUID idListe;

    //@NotEmpty
    private String type;
    
}