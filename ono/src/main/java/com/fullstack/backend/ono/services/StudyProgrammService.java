package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.models.converters.StudyProgrammConverter;
import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyProgrammService implements BaseService {

    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgrammConverter studyProgramConverter;

    /**
     * Transaction pour la création d'un programme d'étude
     * @param dto
     * @param userDto
     * @return StudyProgramDto
     */
    @Transactional
    public StudyProgramDto registerStudyProgram(StudyProgramDto dto, UserDto userDto){
        log.info("Registering programm of study : {}", dto.getName());

        studyProgramRepository.findByOwnerName(userDto.getId(), dto.getName())
        .ifPresent(studyProg ->{
                log.info("This programm already exists : {}", studyProg.getName());
                throw new NotFoundException(StudyProgramErrorCode.ALREADY_EXISTS);
            });
        
        StudyProgram sP = StudyProgram.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .idOwner(userDto.getId())
            .prive(dto.isPrive())
            .build();
        
        return studyProgramConverter.convert(studyProgramRepository.save(sP));    
    }

    /**
     * Transaction pour effectuer des modifications sur un programme d'étude
     * @param idProgram
     * @param dto
     * @return StudyProgramDto
     */
    @Transactional
    public StudyProgramDto uptadeProgram(UUID idProgram, StudyProgramDto dto){
        log.info("Updating study program : {}", dto.getName());

        StudyProgram sp  = studyProgramRepository.findById(idProgram)
                            .orElseThrow(()-> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
                            
        
        sp.setName(dto.getName());
        sp.setDescription(dto.getDescription());

        return studyProgramConverter.convert(studyProgramRepository.save(sp));
    }

    /**
     * Transaction pour obtenir un programme d'etude
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public StudyProgramDto getStudyProgramById(UUID id){
        log.info("Retrieving programm by id : {}", id);

        return studyProgramRepository.findById(id)
                .map(studyProgramConverter::convert)
                .orElseThrow(()-> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }

    /**
     * Transaction pour obtenir un programme d'etude
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public StudyProgramDto getStudyProgramByOwnerName(String name, UserDto user){
        log.info("Retrieving programm by name : {}", name);

        return studyProgramRepository.findByOwnerName(user.getId(), name)
                .map(studyProgramConverter::convert)
                .orElseThrow(()-> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }
    
    /**
     * Transaction pour obtenir la liste des programmes pour un utilisateur
     * @param userDto
     * @return List<StudyProgramDto>
     */
    @Transactional(readOnly = true)
    public List<StudyProgramDto> getAllStudyProgramByOwner(UserDto userDto){
        log.info("Finding all study program of : {}", userDto.getUsername());

       List<StudyProgram> listSP = studyProgramRepository.findAllByOwner(userDto.getId());
       
       return listSP.stream().map(studyProgramConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * Transaction pour supprimer un programme d'étude
     * @param id
     * @return StudyProgramDto
     */
    public StudyProgramDto deleteStudyProgram(UUID id){
        log.info("Deleting programm by id", id);

        StudyProgramDto dto = studyProgramRepository.findById(id)
                .map(studyProgramConverter::convert)
                .orElseThrow(()-> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));

        studyProgramRepository.deleteById(id);

        return dto;

    }


    
}
