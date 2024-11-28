package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.constants.TypeVocabulary;
import com.fullstack.backend.ono.models.entities.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary,UUID> {

    Optional<Vocabulary> findByWordAndListVocaId(String mot, UUID list);

    List<Vocabulary> findAllByListVocaId(UUID idListe);

    List<Vocabulary> findAllByType(TypeVocabulary type);

    List<Vocabulary> findAllByListVocaIdAndType(UUID idListe, TypeVocabulary type);
    
    
}
