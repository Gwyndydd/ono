package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.VocabularyErrorCode;
import com.fullstack.backend.ono.models.constants.TypeVocabulary;
import com.fullstack.backend.ono.models.converters.VocabularyConverter;
import com.fullstack.backend.ono.models.dtos.VocabularyDto;
import com.fullstack.backend.ono.models.entities.Vocabulary;
import com.fullstack.backend.ono.models.entities.VocabularyList;
import com.fullstack.backend.ono.repositories.VocaListRepository;
import com.fullstack.backend.ono.repositories.VocabularyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyService implements BaseService {

    private final VocabularyConverter vocabularyConverter;
    private final VocabularyRepository vocabularyRepository;
    private final VocaListRepository vocaListRepository;

    /**
     * 
     * @param dto
     * @param idList
     * @return
     */
    @Transactional
    public VocabularyDto registerVocabulary(VocabularyDto dto/* , UUID idList*/){
        log.info("Registering vocabulaire : {}", dto.getWord());

        if(dto.getType()== null){
            dto.setType("Unknow");
        }

        vocabularyRepository.findByWordAndListVocaId(dto.getWord(), dto.getIdListe())
        .ifPresent(voca ->{
                log.info("This word already exists : {}", voca.getWord());
                throw new NotFoundException(VocabularyErrorCode.ALREADY_EXISTS);
            });
        
        Vocabulary sP = Vocabulary.builder()
            .word(dto.getWord())
            .definition(dto.getDefinition())
            .listVoca(getVocaListOrError(dto.getIdListe()))
            .type(TypeVocabulary.getByName(dto.getType()))
            .build();
        
        return vocabularyConverter.convert(vocabularyRepository.save(sP));    
    }

    /**
     * 
     * @param idVoca
     * @param dto
     * @return
     */
    @Transactional
    public VocabularyDto uptadeVocabulary(UUID idVoca, VocabularyDto dto){
        log.info("Updating vocabulary : {}", dto.getWord());

        Vocabulary sp  = vocabularyRepository.findById(idVoca)
                            .orElseThrow(()-> new NotFoundException(VocabularyErrorCode.NOT_FOUND));
        
        vocabularyRepository.findByWordAndListVocaId(dto.getWord(), sp.getListVoca().getId())
                            .ifPresent((voca) ->{
                                    if(sp.getId() == voca.getId()){
                                        log.info("This word already exists : {}", voca.getWord());
                                        throw new NotFoundException(VocabularyErrorCode.ALREADY_EXISTS);
                                    }
                                });
                            
        
        sp.setType(TypeVocabulary.getByName(dto.getType()));
        sp.setWord(dto.getWord());
        sp.setDefinition(dto.getDefinition());

        return vocabularyConverter.convert(vocabularyRepository.save(sp));
    }

    /**
     * 
     * @param idVoca
     * @param dto
     * @return
     */
    @Transactional
    public VocabularyDto deleteVocabulary(UUID idVoca){
        log.info("deleting vocabulary : {}", idVoca);

        Vocabulary sp  = vocabularyRepository.findById(idVoca)
                            .orElseThrow(()-> new NotFoundException(VocabularyErrorCode.NOT_FOUND));
                            
        vocabularyRepository.deleteById(idVoca);
        
        return vocabularyConverter.convert(sp);
    }

        /**
     * 
     * @param idVoca
     * @param dto
     * @return
     */
    @Transactional
    public List<VocabularyDto> deleteAllVocabularyFromList(UUID vocaList){
        log.info("deleting vocabulary : {}", vocaList);

        List<Vocabulary> listVoca  = vocabularyRepository.findAllByListVocaId(vocaList);
                  
        Long nb = vocabularyRepository.deleteAllByListVocaId(vocaList);
        
        return listVoca.stream().map(vocabularyConverter::convert).collect(Collectors.toList());
    }

    /**
     * 
     * @param word
     * @param idList
     * @return
     */
    @Transactional(readOnly = true)
    public VocabularyDto getVocabularybyWordOwner(String word, UUID idList){
        log.info("Retrieving vocabulary by word : {}", word);

        return vocabularyRepository.findByWordAndListVocaId(word, idList)
                .map(vocabularyConverter::convert)
                .orElseThrow(()-> new NotFoundException(VocabularyErrorCode.NOT_FOUND));
    }

    /**
     * 
     * @param idList
     * @return
     */
    @Transactional(readOnly = true)
    public List<VocabularyDto> getAllVocabularyinList(UUID idList){
        log.info("Finding all vocabulary from: {}", idList);

       List<Vocabulary> listVoca = vocabularyRepository.findAllByListVocaId(idList);
       
       return listVoca.stream().map(vocabularyConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * 
     * @param idVoca
     * @return
     */
    @Transactional(readOnly = true)
    public VocabularyDto getVocabularybyId(UUID idVoca){
        log.info("Finding all vocabulary from: {}", idVoca);

       Vocabulary voca = vocabularyRepository.findById(idVoca)
                        .orElseThrow(()-> new NotFoundException(VocabularyErrorCode.NOT_FOUND));;
       
       return vocabularyConverter.convert(voca);
    }


    /**
     * 
     * @param idList
     * @param typeString
     * @return
     */
    @Transactional(readOnly = true)
    public List<VocabularyDto> getAllVocabularyinListByType(UUID idList, String typeString){
        log.info("Finding all vocabulary of type {} from: {}", typeString, idList);

        TypeVocabulary type = TypeVocabulary.getByName(typeString);

       List<Vocabulary> listVoca = vocabularyRepository.findAllByListVocaIdAndType(idList,type);
       
       return listVoca.stream().map(vocabularyConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * Get la liste de vocabulaire avec son id ou une erreur
     * @param vocaListId
     * @return
     */
    private VocabularyList getVocaListOrError(UUID vocaListId) {
        return vocaListRepository.findById(vocaListId)
                .orElseThrow(() -> new NotFoundException(VocabularyErrorCode.NOT_FOUND));
    }

    

    
}
