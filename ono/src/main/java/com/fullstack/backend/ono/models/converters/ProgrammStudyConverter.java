package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;

@Component
public class ProgrammStudyConverter implements Converter<StudyProgram,StudyProgramDto>{

    @Override
    public StudyProgramDto convert(StudyProgram studyProgram) {
        return StudyProgramDto.builder()
                .id(studyProgram.getId())
                .nom(studyProgram.getNom())
                .idOwner(studyProgram.getIdOwner())
                .prive(studyProgram.getPrive())
                .build();
    }

    
    
}
