package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.GrammarListDto;
import com.fullstack.backend.ono.models.entities.GrammarList;

@Component
public class GrammarListConverter implements Converter<GrammarList, GrammarListDto> {

    @Override
    public GrammarListDto convert(GrammarList listGrammar) {
        return GrammarListDto.builder()
                .id(listGrammar.getId())
                .name(listGrammar.getName())
                .langueEtudie(listGrammar.getLangueEtudie().name())
                .idOwner(listGrammar.getOwner().getId())
                .prive(listGrammar.getPrive())
                .build();
    }
    
}
