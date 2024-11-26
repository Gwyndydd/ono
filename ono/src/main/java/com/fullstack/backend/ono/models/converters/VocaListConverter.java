package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;

import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.entities.ListVocabulary;

public class VocaListConverter implements Converter<ListVocabulary,VocaListDto> {

    @Override
    public VocaListDto convert(ListVocabulary listVoca) {
        return VocaListDto.builder()
                .id(listVoca.getId())
                .nom(listVoca.getNom())
                .langueEtudie(listVoca.getLangueEtudie())
                .langueDefinition(listVoca.getLangueDefinition())
                .idProgrammeEtude(listVoca.getIdProgrammeEtude())
                .idOwner(listVoca.getIdOwner())
                .prive(listVoca.getPrive())
                .build();
    }
    
}
