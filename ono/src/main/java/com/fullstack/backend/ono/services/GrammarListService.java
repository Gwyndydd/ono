package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.GrammarListErrorCode;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.exceptions.errors.UserErrorCode;
import com.fullstack.backend.ono.models.constants.Langues;
import com.fullstack.backend.ono.models.converters.GrammarListConverter;
import com.fullstack.backend.ono.models.dtos.GrammarListDto;
import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.entities.GrammarList;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.repositories.GrammarListRepository;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrammarListService implements BaseService {

    private final GrammarListRepository listGrammarRepository;
    private final GrammarListConverter listGrammarConverter;
    private final StudyProgramRepository studyProgrammRepository;
    private final UserRepository userRepository;

    /**
     * 
     * @param gDto
     * @param user
     * @return
     */
    @Transactional
    public GrammarListDto registerGrammarList(GrammarListDto gDto, UserDto user){
        log.info("Registering list of grammar : {}", gDto.getName());

        Langues langue = Langues.getByName(gDto.getLangueEtudie());

        listGrammarRepository.findByNameAndOwnerId(gDto.getName(),user.getId())
            .ifPresent( list -> {
                log.info("This list of grammar already exist : {}", list.getName());
                throw new NotFoundException(GrammarListErrorCode.ALREADY_EXISTS);
            });

        GrammarList gList = GrammarList.builder()
                .name(gDto.getName())
                .langueEtudie(langue)
                .studyProgram((gDto.getIdProgrammeEtude() == null) ? null: getStudyProgrammOrError(gDto.getIdProgrammeEtude()))
                .owner(getUserOrError(user.getId()))
                .prive(gDto.getPrive())
                .build();

        return listGrammarConverter.convert(listGrammarRepository.save(gList));

    }

    /**
     * 
     * @param dto
     * @param idGrammarList
     * @return
     */
    @Transactional
    public GrammarListDto addStudyProgramm(StudyProgramDto dto, UUID idGrammarList){

        log.info("Add study program {} to list of grammar : {}", dto.getName(), idGrammarList);

        GrammarList glist  = listGrammarRepository.findById(idGrammarList)
                            .orElseThrow(()-> new NotFoundException(GrammarListErrorCode.NOT_FOUND));
                            
        
        glist.setStudyProgram((dto.getId() == null) ? null: getStudyProgrammOrError(dto.getId()));

        return listGrammarConverter.convert(listGrammarRepository.save(glist));

    }

    /**
     * 
     * @param dto
     * @param idGrammarList
     * @return
     */
    @Transactional
    public GrammarListDto updateGrammarList(UUID idGrammarList, GrammarListDto dto){

        log.info("Update list of grammar : {}", idGrammarList);

        GrammarList glist  = listGrammarRepository.findById(idGrammarList)
                            .orElseThrow(()-> new NotFoundException(GrammarListErrorCode.NOT_FOUND));
                            
        glist.setName(dto.getName());
        glist.setLangueEtudie(Langues.getByName(dto.getName()));
        glist.setPrive(dto.getPrive());

        return listGrammarConverter.convert(listGrammarRepository.save(glist));

    }

    /**
     * 
     * @param gDto
     * @return
     */
    @Transactional
    public GrammarListDto deleGrammarList(GrammarListDto gDto){

        GrammarList glist  = listGrammarRepository.findById(gDto.getId())
                            .orElseThrow(()-> new NotFoundException(GrammarListErrorCode.NOT_FOUND));
        
        return listGrammarConverter.convert(glist);
        
    }


    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly = true)
    public GrammarListDto getGrammarListId(UUID id){
        log.info("Retrieving programm by id : {}", id);

        return listGrammarRepository.findById(id)
                .map(listGrammarConverter::convert)
                .orElseThrow(()-> new NotFoundException(GrammarListErrorCode.NOT_FOUND));
    }

        /**
     * Transaction pour obtenir un programme d'etude
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public GrammarListDto getGrammarListByOwnerName(String name, UserDto user){
        log.info("Retrieving programm by name : {}", name);

        return listGrammarRepository.findByNameAndOwnerId(name,user.getId())
                .map(listGrammarConverter::convert)
                .orElseThrow(()-> new NotFoundException(GrammarListErrorCode.NOT_FOUND));
    }
    
    /**
     * Transaction pour obtenir la liste des programmes pour un utilisateur
     * @param userDto
     * @return List<StudyProgramDto>
     */
    @Transactional(readOnly = true)
    public List<GrammarListDto> getAllGrammarListByOwner(UserDto userDto){
        log.info("Finding all study program of : {}", userDto.getUsername());

       List<GrammarList> listG = listGrammarRepository.findAllByOwnerId(userDto.getId());
       
       return listG.stream().map(listGrammarConverter::convert).collect(Collectors.toList());
    
    }

    

    private User getUserOrError(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
    }

    private StudyProgram getStudyProgrammOrError(UUID studyProgrammId) {
        return studyProgrammRepository.findById(studyProgrammId)
                .orElseThrow(() -> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }


    
}
