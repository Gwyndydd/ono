package com.fullstack.backend.ono.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.models.constants.TypeVocabulary;
import com.fullstack.backend.ono.models.converters.VocabularyConverter;
import com.fullstack.backend.ono.models.dtos.VocabularyDto;
import com.fullstack.backend.ono.models.entities.Vocabulary;
import com.fullstack.backend.ono.models.entities.VocabularyList;
import com.fullstack.backend.ono.repositories.VocaListRepository;
import com.fullstack.backend.ono.repositories.VocabularyRepository;

import io.jsonwebtoken.Clock;

@ExtendWith(MockitoExtension.class)
class VocabularyServiceTest {

    @Mock
    private VocabularyConverter vocabularyConverter;

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private VocaListRepository vocaListRepository;

    @InjectMocks
    private VocabularyService vocabularyService;

    @Mock
    static Clock clock;

    private VocabularyDto defaultVocabularyDto;
    private Vocabulary defaultVocabulary;
    private VocabularyList defaultVocabularyList;

    static UUID defaultIdVocabulary = UUID.randomUUID();
    static UUID defaultIdList = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        defaultVocabularyList = createVocabularyList(defaultIdList, "Default List");

        defaultVocabulary = createVocabulary(
            defaultIdVocabulary, 
            "Word", 
            "Definition", 
            TypeVocabulary.NAME, 
            defaultVocabularyList
        );

        defaultVocabularyDto = createVocabularyDto(
            defaultIdVocabulary, 
            "Word", 
            "Definition", 
            "NOUN", 
            defaultIdList
        );
    }

    @Test
    void registerVocabulary_ok_returnDto() {
        
        Mockito.when(vocabularyRepository.findByWordAndListVocaId("Word", defaultIdList))
            .thenReturn(Optional.empty());
        Mockito.when(vocaListRepository.findById(defaultIdList))
            .thenReturn(Optional.of(defaultVocabularyList));
        Mockito.when(vocabularyRepository.save(any(Vocabulary.class)))
            .thenReturn(defaultVocabulary);
        Mockito.when(vocabularyConverter.convert(defaultVocabulary))
            .thenReturn(defaultVocabularyDto);

        
        VocabularyDto result = vocabularyService.registerVocabulary(defaultVocabularyDto);

        Mockito.verify(vocabularyRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(vocabularyRepository,Mockito.times(1)).findByWordAndListVocaId(Mockito.any(),Mockito.any());
        Mockito.verify(vocabularyConverter, Mockito.times(1)).convert(Mockito.any());
        
        assertNotNull(result);
        assertEquals("Word", result.getWord());
        
    }

    @Test
    void registerVocabulary_DuplicateWord_throwError() {
        
        Mockito.when(vocabularyRepository.findByWordAndListVocaId("Word", defaultIdList))
            .thenReturn(Optional.of(defaultVocabulary));

        
        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyService.registerVocabulary(defaultVocabularyDto);
        });
    }

    @Test
    void uptadeVocabulary_ok_returnDto() {
        
        VocabularyDto updatedDto = createVocabularyDto(
            defaultIdVocabulary, 
            "Updated Word", 
            "Updated Definition", 
            "VERB", 
            defaultIdList
        );

        Mockito.when(vocabularyRepository.findById(defaultIdVocabulary))
            .thenReturn(Optional.of(defaultVocabulary));
        Mockito.when(vocabularyRepository.findByWordAndListVocaId("Updated Word", defaultIdList))
            .thenReturn(Optional.empty());
        Mockito.when(vocabularyRepository.save(any(Vocabulary.class)))
            .thenReturn(defaultVocabulary);
        Mockito.when(vocabularyConverter.convert(defaultVocabulary))
            .thenReturn(updatedDto);

        
        VocabularyDto result = vocabularyService.uptadeVocabulary(defaultIdVocabulary, updatedDto);

        
        assertNotNull(result);
        assertEquals("Updated Word", result.getWord());

        Mockito.verify(vocabularyRepository,Mockito.times(1)).findByWordAndListVocaId(Mockito.any(),Mockito.any());
        Mockito.verify(vocabularyRepository,Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(vocabularyConverter, Mockito.times(1)).convert(Mockito.any());
    }

    @Test
    void uptadeVocabulary_DublicateWord_throwError() {
        
        Vocabulary duplicateVocabulary = createVocabulary(
            defaultIdVocabulary, 
            "Duplicate Word", 
            "Duplicate Definition", 
            TypeVocabulary.NAME, 
            defaultVocabularyList
        );

        Mockito.when(vocabularyRepository.findById(defaultIdVocabulary))
            .thenReturn(Optional.of(defaultVocabulary));
        Mockito.when(vocabularyRepository.findByWordAndListVocaId("Duplicate Word", defaultIdList))
            .thenReturn(Optional.of(duplicateVocabulary));

        VocabularyDto updatedDto = createVocabularyDto(
            defaultIdVocabulary, 
            "Duplicate Word", 
            "Updated Definition", 
            "Nom", 
            defaultIdList
        );

        
        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyService.uptadeVocabulary(defaultIdVocabulary, updatedDto);
        });
        Mockito.verify(vocabularyRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyRepository, Mockito.times(1)).findByWordAndListVocaId(Mockito.any(),Mockito.any());
        
    }

    @Test
    void deleteVocabulary_ok_returnDto() {
        
        Mockito.when(vocabularyRepository.findById(defaultIdVocabulary))
            .thenReturn(Optional.of(defaultVocabulary));
        Mockito.when(vocabularyConverter.convert(defaultVocabulary))
            .thenReturn(defaultVocabularyDto);

        
        VocabularyDto result = vocabularyService.deleteVocabulary(defaultIdVocabulary);

        
        assertNotNull(result);
        assertEquals(result, defaultVocabularyDto);
        Mockito.verify(vocabularyRepository,Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyRepository,Mockito.times(1)).deleteById(Mockito.any());
        Mockito.verify(vocabularyConverter, Mockito.times(1)).convert(Mockito.any());
    }

    @Test
    void deleteVocabulary_NotFound_ThrowError() {
        
        Mockito.when(vocabularyRepository.findById(defaultIdVocabulary))
            .thenReturn(Optional.empty());

        
        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyService.deleteVocabulary(defaultIdVocabulary);
        });

        Mockito.verify(vocabularyRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void getAllVocabularyinList_shouldRetrieveAllVocabularyFromList() {
        
        List<Vocabulary> vocabularies = List.of(defaultVocabulary);
        Mockito.when(vocabularyRepository.findAllByListVocaId(defaultIdList))
            .thenReturn(vocabularies);
        Mockito.when(vocabularyConverter.convert(defaultVocabulary))
            .thenReturn(defaultVocabularyDto);

        
        List<VocabularyDto> result = vocabularyService.getAllVocabularyinList(defaultIdList);

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Word", result.get(0).getWord());

        Mockito.verify(vocabularyRepository,Mockito.times(1)).findAllByListVocaId(Mockito.any());
        Mockito.verify(vocabularyConverter, Mockito.times(1)).convert(Mockito.any());
    }

    // Utilitaires pour les builders
    private VocabularyList createVocabularyList(UUID id, String name) {
        return VocabularyList.builder()
            .id(id)
            .name(name)
            .build();
    }

    private Vocabulary createVocabulary(UUID id, String word, String definition, TypeVocabulary type, VocabularyList list) {
        return Vocabulary.builder()
            .id(id)
            .word(word)
            .definition(definition)
            .type(type)
            .listVoca(list)
            .build();
    }

    private VocabularyDto createVocabularyDto(UUID id, String word, String definition, String type, UUID idListe) {
        return VocabularyDto.builder()
            .word(word)
            .definition(definition)
            .type(type)
            .idListe(idListe)
            .build();
    }
}
