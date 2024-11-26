package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;

import com.fullstack.backend.ono.models.dtos.GramListDto;
import com.fullstack.backend.ono.models.entities.ListGrammar;

public class GrammarListConverter implements Converter<ListGrammar, GramListDto> {

    @Override
    public GramListDto convert(ListGrammar listGrammar) {
        return GramListDto.builder()
                .id(listGrammar.getId())
                .nom(listGrammar.getNom())
                .langueEtudie(listGrammar.getLangueEtudie())
                .idOwner(listGrammar.getIdOwner())
                .prive(listGrammar.getPrive())
                .build();
    }
    
}
