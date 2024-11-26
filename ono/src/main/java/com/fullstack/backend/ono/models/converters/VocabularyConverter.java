package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.VocabularyDto;
import com.fullstack.backend.ono.models.entities.Vocabulary;

@Component
public class VocabularyConverter implements Converter<Vocabulary, VocabularyDto> {

    @Override
    public VocabularyDto convert(Vocabulary voca){

        return VocabularyDto.builder()
                .idVoca(voca.getIdVoca())
                .mot(voca.getMot())
                .definition(voca.getDefinition())
                .idListe(voca.getIdListeVoca())
                .type(voca.getType())
                .build();
    }
    
}
