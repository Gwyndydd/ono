package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.exceptions.errors.UserErrorCode;
import com.fullstack.backend.ono.models.converters.StudyProgrammConverter;
import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyProgrammService implements BaseService {

    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgrammConverter studyProgramConverter;
    private final UserRepository userRepository;

    /**
     * Transaction pour la création d'un programme d'étude
     * @param dto
     * @param userDto
     * @return StudyProgramDto
     */
    @Transactional
    public StudyProgramDto registerStudyProgram(StudyProgramDto dto/*, UserDto userDto*/){
        log.info("Registering programm of study : {}", dto.getName());

        studyProgramRepository.findByOwnerIdAndName(dto.getIdOwner(), dto.getName())
        .ifPresent(studyProg ->{
                log.info("This programm already exists : {}", studyProg.getName());
                throw new NotFoundException(StudyProgramErrorCode.ALREADY_EXISTS);
            });
        
        StudyProgram sP = StudyProgram.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .owner(getUserOrError(dto.getIdOwner()))
            .visibility(dto.isPrive())
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
    public StudyProgramDto getStudyProgramByOwnerName(String name, UUID user){
        log.info("Retrieving programm by name : {}", name);

        return studyProgramRepository.findByOwnerIdAndName(user, name)
                .map(studyProgramConverter::convert)
                .orElseThrow(()-> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }

        /**
     * Transaction pour obtenir un programme d'etude
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public List<StudyProgramDto> searchStudyProgram(String search, UUID user){
        log.info("Retrieving list of programm containing search : {}", search);

        return studyProgramRepository.searchByVocaAndUserNameWithVisibility(search, user)
                .stream()
                .map(studyProgramConverter::convert)
                .collect(Collectors.toList());
    }
    
    /**
     * Transaction pour obtenir la liste des programmes pour un utilisateur
     * @param UUID idUser
     * @return List<StudyProgramDto>
     */
    @Transactional(readOnly = true)
    public List<StudyProgramDto> getAllStudyProgramByOwner(UUID id){
        log.info("Finding all study program of : {}",id);

       List<StudyProgram> listSP = studyProgramRepository.findAllByOwnerId(id);
       
       return listSP.stream().map(studyProgramConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<StudyProgramDto> getAllStudyProgram(){
        log.info("Finding all study program");

       List<StudyProgram> listSP = studyProgramRepository.findAll();
       
       return listSP.stream().map(studyProgramConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * 
     * @param visibility
     * @return
     */
    @Transactional(readOnly = true)
    public List<StudyProgramDto> getAllStudyProgramByVisibility(boolean visibility){
        log.info("Finding all study program with visibility of : {}", visibility);

       List<StudyProgram> listSP = studyProgramRepository.findAllByVisibilityOrderByUpdatedAtDesc(visibility);
       
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

    public List<StudyProgramDto> search(String search, UUID idUser){
        log.info("Search programm by ", search);

        List<StudyProgram> studyPrograms = studyProgramRepository.searchByVocaAndUserNameWithVisibility(search, idUser);
        return studyPrograms.stream().map(studyProgramConverter::convert).collect(Collectors.toList());
    }

    private User getUserOrError(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
    }


    
}
