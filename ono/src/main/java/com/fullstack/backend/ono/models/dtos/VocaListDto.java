package com.fullstack.backend.ono.models.dtos;

import java.util.UUID;

import com.fullstack.backend.ono.models.constants.Langues;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocaListDto {

    private UUID id;

    @NotEmpty
    private String nom;

    @NotEmpty
    private Langues langueEtudie;

    @NotEmpty
    private Langues langueDefinition;

    private UUID idProgrammeEtude;

    @NotEmpty
    private UUID idOwner;

    private Boolean prive;
    
}
