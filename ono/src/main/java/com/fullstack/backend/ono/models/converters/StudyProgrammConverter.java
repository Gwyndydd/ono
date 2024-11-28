package com.fullstack.backend.ono.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;

@Component
public class StudyProgrammConverter implements Converter<StudyProgram,StudyProgramDto>{

    @Override
    public StudyProgramDto convert(StudyProgram studyProgram) {
        return StudyProgramDto.builder()
                .id(studyProgram.getId())
                .name(studyProgram.getName())
                .description(studyProgram.getDescription())
                .idOwner(studyProgram.getOwner().getId())
                .prive(studyProgram.getPrive())
                .build();
    }

    
    
}
