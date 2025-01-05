package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.OwnerError;
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
     * Créer une liste de vocabulaire
     * @param gDto
     * @param user
     * @return
     */
    @Transactional
    public VocaListDto registerVocabularyList(VocaListDto gDto){
        log.info("Registering list of Vocabulary : {}", gDto.getName());

        Langues langueEtudie = Langues.getByName(gDto.getLangueEtudie());
        Langues langueDef = Langues.getByName(gDto.getLangueDefinition());

        listVocaRepository.findByOwnerIdAndName(gDto.getIdOwner(), gDto.getName())
            .ifPresent( list -> {
                log.info("This list of Vocabulary already exist : {}", list.getName());
                throw new NotFoundException(VocabularyListErrorCode.ALREADY_EXISTS);
            });

        VocabularyList gList = VocabularyList.builder()
                .name(gDto.getName())
                .langueEtudie(langueEtudie)
                .langueDefinition(langueDef)
                .studyProgram((gDto.getIdProgrammeEtude() == null) ? null : getStudyProgrammOrError(gDto.getIdProgrammeEtude()))
                .owner(getUserOrError(gDto.getIdOwner()))
                .visibility(gDto.getPrive())
                .build();

        return listVocaConverter.convert(listVocaRepository.save(gList));

    }

    /**
     * Ajouter la liste de vocabulaire à programme d'étude
     * @param dto
     * @param idVocabularyList
     * @return
     */
    @Transactional
    public VocaListDto addStudyProgramm(UUID studyProgramUUID, UUID idVocabularyList){

        log.info("Add study program {} to list of Vocabulary : {}", studyProgramUUID, idVocabularyList);

        VocabularyList glist  = listVocaRepository.findById(idVocabularyList)
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
                            
        
        glist.setStudyProgram((studyProgramUUID == null) ? null :getStudyProgrammOrError(studyProgramUUID));

        return listVocaConverter.convert(listVocaRepository.save(glist));

    }

    /**
     * Mise à jour de la liste de vocabulaire
     * @param dto
     * @param idVocabularyList
     * @return
     */
    @Transactional
    public VocaListDto updateVocabularyList(UUID idVocabularyList, VocaListDto dto){

        log.info("Update list of Vocabulary : {}", idVocabularyList);

        VocabularyList glist  = listVocaRepository.findById(idVocabularyList)
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));

        
        listVocaRepository.findByOwnerIdAndName(glist.getOwner().getId(), dto.getName())
                            .ifPresent( list -> {
                                if(list.getId()!=idVocabularyList){
                                    log.info("This list of Vocabulary already exist : {}", list.getName());
                                    throw new NotFoundException(VocabularyListErrorCode.ALREADY_EXISTS);
                                }
                            });
                
        
        //log.info("Langue etudie :{}", dto.getLangueEtudie());

        glist.setName(dto.getName());
        glist.setLangueEtudie(Langues.getByName(dto.getLangueEtudie()));
        glist.setLangueDefinition(Langues.getByName(dto.getLangueDefinition()));
        glist.setVisibility(dto.getPrive());

        return listVocaConverter.convert(listVocaRepository.save(glist));

    }

    /**
     * Supprimer la liste de vocabulaire
     * @param gDto
     * @return
     */
    @Transactional
    public VocaListDto deleVocabularyList(UUID vocaListId){

        VocabularyList glist  = listVocaRepository.findById(vocaListId)
                            .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
        listVocaRepository.deleteById(vocaListId);
        
        return listVocaConverter.convert(glist);
        
    }


    /**
     * Obtenir une liste de vocabulaire par son id
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
     * Transaction pour obtenir un programme d'etude par son nom et id de l'utilisateur
     * @param id
     * @return StudyProgramDto
     */
    @Transactional(readOnly = true)
    public VocaListDto getVocabularyListByOwnerName(String name, UUID idUser){
        log.info("Retrieving programm by name : {}", name);

        return listVocaRepository.findByOwnerIdAndName(idUser, name)
                .map(listVocaConverter::convert)
                .orElseThrow(()-> new NotFoundException(VocabularyListErrorCode.NOT_FOUND));
    }
    
    /**
     * Transaction pour obtenir la liste des programmes pour un utilisateur
     * @param userDto
     * @return List<StudyProgramDto>
     */
    @Transactional(readOnly = true)
    public List<VocaListDto> getAllVocabularyListByOwner(UUID idUser){
        log.info("Finding all vocabulary list of : {}", idUser);

       List<VocabularyList> listG = listVocaRepository.findAllByOwnerId(idUser);
       
       return listG.stream().map(listVocaConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * Obtenir obtenir tout les listes de vocabulaire par visibilité
     * @param visibility
     * @return
     */
    @Transactional(readOnly = true)
    public List<VocaListDto> getAllVocabularyListByVisibility(boolean visibility){
        log.info("Finding all vocabulary list with visibility of : {}", visibility);

       List<VocabularyList> listVoca = listVocaRepository.findAllByVisibilityOrderByUpdatedAtDesc(visibility);
       
       return listVoca.stream().map(listVocaConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * Obtenir l'utilisateur
     * @param userId
     * @return
     */
    private User getUserOrError(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
    }

    /**
     * Obtenir un programme d'étude
     * @param studyProgrammId
     * @return
     */
    private StudyProgram getStudyProgrammOrError(UUID studyProgrammId) {
        return studyProgrammRepository.findById(studyProgrammId)
                .orElseThrow(() -> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }

    
}
