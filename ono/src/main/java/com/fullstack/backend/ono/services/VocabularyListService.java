package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.exceptions.errors.UserErrorCode;
import com.fullstack.backend.ono.exceptions.errors.VocabularyListErrorCode;
import com.fullstack.backend.ono.models.constants.Langues;
import com.fullstack.backend.ono.models.converters.VocaListConverter;
import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.models.entities.VocabularyList;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;
import com.fullstack.backend.ono.repositories.VocaListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyListService implements BaseService {

    private final VocaListRepository listVocaRepository;
    private final VocaListConverter listVocaConverter;
    private final StudyProgramRepository studyProgrammRepository;
    private final UserRepository userRepository;

    /**
     * 
     * @param gDto
     * @param user
     * @return
     */
    @Transactional
    public VocaListDto registerVocabularyList(VocaListDto gDto, UserDto user){
        log.info("Registering list of Vocabulary : {}", gDto.getName());

        Langues langueEtudie = Langues.getByName(gDto.getLangueEtudie());
        Langues langueDef = Langues.getByName(gDto.getLangueDefinition());

        listVocaRepository.findByOwnerIdAndName(user.getId(), gDto.getName())
            .ifPresent( list -> {
                log.info("This list of Vocabulary already exist : {}", list.getName());
                throw new NotFoundException(VocabularyListErrorCode.ALREADY_EXISTS);
            });

        VocabularyList gList = VocabularyList.builder()
                .name(gDto.getName())
                .langueEtudie(langueEtudie)
                .langueDefinition(langueDef)
                .studyProgram((gDto.getIdProgrammeEtude() == null) ? null : getStudyProgrammOrError(gDto.getIdProgrammeEtude()))
                .owner(getUserOrError(user.getId()))
                .prive(gDto.getPrive())
                .build();

        return listVocaConverter.convert(listVocaRepository.save(gList));

    }

    /**
     * 
     * @param dto
     * @param idVocabularyList
     * @return
     */
    @Transactional
    public VocaListDto addStudyProgramm(StudyProgramDto dto, UUID idVocabularyList){

        log.info("Add study program {} to list of Vocabulary : {}", dto.getName(), idVocabularyList);

        VocabularyList glist  = listVocaRepository.findById(idVocabularyList)
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
                            
        
        glist.setStudyProgram((dto.getId() == null) ? null :getStudyProgrammOrError(dto.getId()));

        return listVocaConverter.convert(listVocaRepository.save(glist));

    }

    /**
     * 
     * @param dto
     * @param idVocabularyList
     * @return
     */
    @Transactional
    public VocaListDto updateVocabularyList(UUID idVocabularyList, VocaListDto dto){

        log.info("Update list of Vocabulary : {}", idVocabularyList);

        VocabularyList glist  = listVocaRepository.findById(idVocabularyList)
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
                            
        glist.setName(dto.getName());
        glist.setLangueEtudie(Langues.getByName(dto.getName()));
        glist.setPrive(dto.getPrive());

        return listVocaConverter.convert(listVocaRepository.save(glist));

    }

    /**
     * 
     * @param gDto
     * @return
     */
    @Transactional
    public VocaListDto deleVocabularyList(VocaListDto gDto){

        VocabularyList glist  = listVocaRepository.findById(gDto.getId())
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
        
        return listVocaConverter.convert(glist);
        
    }


    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly = true)
    public VocaListDto getVocabularyListId(UUID id){
        log.info("Retrieving programm by id : {}", id);

        return listVocaRepository.findById(id)
                .map(listVocaConverter::convert)
                .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
    }

        /**
     * Transaction pour obtenir un programme d'etude
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public VocaListDto getVocabularyListByOwnerName(String name, UserDto user){
        log.info("Retrieving programm by name : {}", name);

        return listVocaRepository.findByOwnerIdAndName(user.getId(), name)
                .map(listVocaConverter::convert)
                .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
    }
    
    /**
     * Transaction pour obtenir la liste des programmes pour un utilisateur
     * @param userDto
     * @return List<StudyProgramDto>
     */
    @Transactional(readOnly = true)
    public List<VocaListDto> getAllVocabularyListByOwner(UserDto userDto){
        log.info("Finding all study program of : {}", userDto.getUsername());

       List<VocabularyList> listG = listVocaRepository.findAllByOwnerId(userDto.getId());
       
       return listG.stream().map(listVocaConverter::convert).collect(Collectors.toList());
    
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
