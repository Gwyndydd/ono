package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.GrNotionDto;
import com.fullstack.backend.ono.models.entities.GrammarNotion;

@Component
public class GrNotionConverter implements Converter<GrammarNotion, GrNotionDto> {

    @Override
    public GrNotionDto convert(GrammarNotion notion) {
        
        return GrNotionDto.builder()
                .id(notion.getId())
                .concept(notion.getConcept())
                .grammarNotion(notion.getGrammarNotion())
                .build();
    }
    
}
