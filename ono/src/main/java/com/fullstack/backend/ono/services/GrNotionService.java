package com.fullstack.backend.ono.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.GrNotionErrorCode;
import com.fullstack.backend.ono.exceptions.errors.GrammarErrorCode;
import com.fullstack.backend.ono.models.converters.GrNotionConverter;
import com.fullstack.backend.ono.models.dtos.GrNotionDto;
import com.fullstack.backend.ono.models.entities.Grammar;
import com.fullstack.backend.ono.models.entities.GrammarNotion;
import com.fullstack.backend.ono.repositories.GrNotionRepository;
import com.fullstack.backend.ono.repositories.GrammarRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrNotionService implements BaseService {

    private final GrNotionRepository grNotionRepository;
    private final GrNotionConverter grNotionConverter;
    private final GrammarRepository grammarRepository;


    /**
     * 
     * @param dto
     * @param idGrammar
     * @return
     */
    @Transactional
    public GrNotionDto registerNotion(GrNotionDto dto, UUID idGrammar){

        log.info("Registering notion : {}", dto.getConcept());
        
        GrammarNotion sP = GrammarNotion.builder()
            .concept(dto.getConcept())
            .grammar(getGrammarOrError(idGrammar))
            .build();
        
        return grNotionConverter.convert(grNotionRepository.save(sP));    

    }

    /**
     * 
     * @param dto
     * @param idNotion
     * @return
     */
    @Transactional
    public GrNotionDto deleteNotion(GrNotionDto dto, UUID idNotion){

        log.info("Deleting notion : {}", dto.getConcept());
        
        GrammarNotion sP = grNotionRepository.findById(idNotion)
                        .orElseThrow(()-> new NotFoundException(GrNotionErrorCode.NOT_FOUND));
        
        grNotionRepository.deleteById(idNotion);

        return grNotionConverter.convert(sP);    

    }

    /**
     * 
     * @param idGrammar
     * @return
     */
    @Transactional(readOnly = true)
    public List<GrNotionDto> getAllNotioninGrammar(UUID idGrammar){
        log.info("Finding all notion of : {}", idGrammar);

       List<GrammarNotion> listNotion = grNotionRepository.findAllByGrammarId(idGrammar);
       
       return listNotion.stream().map(grNotionConverter::convert).collect(Collectors.toList());
    
    }

    /**
     * 
     * @param idNotion
     * @return
     */
    @Transactional(readOnly = true)
    public GrNotionDto getbyId(UUID idNotion){
        log.info("Finding notion : {}", idNotion);

       GrammarNotion Notion = grNotionRepository.findById(idNotion)
                    .orElseThrow(()-> new NotFoundException(GrNotionErrorCode.NOT_FOUND));;
       
       return grNotionConverter.convert(Notion);
    
    }

        private Grammar getGrammarOrError(UUID grammarId) {
        return grammarRepository.findById(grammarId)
                .orElseThrow(() -> new NotFoundException(GrammarErrorCode.NOT_FOUND));
    }

}
