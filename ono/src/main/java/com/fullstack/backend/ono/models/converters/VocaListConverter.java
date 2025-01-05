package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.entities.VocabularyList;

@Component
public class VocaListConverter implements Converter<VocabularyList,VocaListDto> {

    @Override
    public VocaListDto convert(VocabularyList listVoca) {
        return VocaListDto.builder()
                .id(listVoca.getId())
                .name(listVoca.getName())
                .langueEtudie(listVoca.getLangueEtudie().name())
                .langueDefinition(listVoca.getLangueDefinition().name())
                .idProgrammeEtude((listVoca.getStudyProgram() == null) ? null : listVoca.getStudyProgram().getId())
                .idOwner(listVoca.getOwner().getId())
                .prive(listVoca.getVisibility())
                .build();
    }
    
}
