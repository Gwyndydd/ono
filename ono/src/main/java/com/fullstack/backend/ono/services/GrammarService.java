package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.GrammarErrorCode;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.models.converters.GrammarConverter;
import com.fullstack.backend.ono.models.dtos.GrammarDto;
import com.fullstack.backend.ono.models.entities.Grammar;
import com.fullstack.backend.ono.models.entities.GrammarList;
import com.fullstack.backend.ono.repositories.GrammarListRepository;
import com.fullstack.backend.ono.repositories.GrammarRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrammarService implements BaseService {

    private final GrammarConverter grammarConverter;
    private final GrammarRepository grammarRepository;

    private final GrammarListRepository grammarListRepository;

    /**
     * 
     * @param dto
     * @param idList
     * @return
     */
    @Transactional
    public GrammarDto registerGrammar(GrammarDto dto, UUID idList){
        log.info("Registering grammaar : {}", dto.getNotion());

        grammarRepository.findByNotionAndGrammarListId(dto.getNotion(), idList)
        .ifPresent(grammar ->{
                log.info("This notion already exists : {}", grammar.getNotion());
                throw new NotFoundException(GrammarErrorCode.ALREADY_EXISTS);
            });
        
        Grammar sP = Grammar.builder()
            .notion(dto.getNotion())
            .grammarList(getGrammarListOrError(idList))
            .build();
        
        return grammarConverter.convert(grammarRepository.save(sP));    
    }

    /**
     * 
     * @param idGrammar
     * @param dto
     * @return
     */
    @Transactional
    public GrammarDto uptadeGrammar(UUID idGrammar, GrammarDto dto){
        log.info("Updating grammar : {}", dto.getNotion());

        Grammar sp  = grammarRepository.findById(idGrammar)
                            .orElseThrow(()-> new NotFoundException(GrammarErrorCode.NOT_FOUND));
                            
        
        sp.setNotion(dto.getNotion());
        sp.setGrammarList(getGrammarListOrError(idGrammar));;

        return grammarConverter.convert(grammarRepository.save(sp));
    }

    /**
     * 
     * @param idGrammar
     * @param dto
     * @return
     */
    @Transactional
    public GrammarDto deleteGrammar(UUID idGrammar, GrammarDto dto){
        log.info("deleting grammar : {}", dto.getNotion());

        Grammar sp  = grammarRepository.findById(idGrammar)
                            .orElseThrow(()-> new NotFoundException(GrammarErrorCode.NOT_FOUND));
                            
        grammarRepository.deleteById(idGrammar);
        
        return grammarConverter.convert(sp);
    }

    /**
     * 
     * @param idList
     * @return
     */
    @Transactional(readOnly = true)
    public List<GrammarDto> getAllGrammarinList(UUID idList){
        log.info("Finding all grammar from: {}", idList);

       List<Grammar> listGrammar = grammarRepository.findAllByGrammarListId(idList);
       
       return listGrammar.stream().map(grammarConverter::convert).collect(Collectors.toList());
    
    }

    /***
     * 
     * @param idGrammar
     * @return
     */
    @Transactional(readOnly = true)
    public GrammarDto getGrammarbyId(UUID idGrammar){
        log.info("Finding all grammar from: {}", idGrammar);

       Grammar grammar = grammarRepository.findById(idGrammar)
                        .orElseThrow(()-> new NotFoundException(GrammarErrorCode.NOT_FOUND));;
       
       return grammarConverter.convert(grammar);
    
    }

    private GrammarList getGrammarListOrError(UUID grammarListId) {
        return grammarListRepository.findById(grammarListId)
                .orElseThrow(() -> new NotFoundException(StudyProgramErrorCode.NOT_FOUND));
    }



    

    
}
