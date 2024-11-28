package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.GrammarDto;
import com.fullstack.backend.ono.models.entities.Grammar;

@Component
public class GrammarConverter implements Converter<Grammar,GrammarDto> {

    @Override
    public GrammarDto convert(Grammar grammaire) {
        
        return GrammarDto.builder()
                .id(grammaire.getId())
                .notion(grammaire.getNotion())
                .idListeGrammaire(grammaire.getGrammarList().getId())
                .build();
    }


    
}
