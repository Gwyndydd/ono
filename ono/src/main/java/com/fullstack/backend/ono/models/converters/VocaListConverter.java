package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;

import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.entities.VocabularyList;

public class VocaListConverter implements Converter<VocabularyList,VocaListDto> {

    @Override
    public VocaListDto convert(VocabularyList listVoca) {
        return VocaListDto.builder()
                .id(listVoca.getId())
                .name(listVoca.getName())
                .langueEtudie(listVoca.getLangueEtudie())
                .langueDefinition(listVoca.getLangueDefinition())
                .idProgrammeEtude(listVoca.getIdProgrammeEtude())
                .idOwner(listVoca.getIdOwner())
                .prive(listVoca.getPrive())
                .build();
    }
    
}
