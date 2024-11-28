package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.constants.TypeVocabulary;
import com.fullstack.backend.ono.models.entities.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary,UUID> {

    Optional<Vocabulary> findbyWordList(String mot, UUID list);

    List<Vocabulary> findAllbyIdListe(UUID idListe);

    List<Vocabulary> findAllbyType(TypeVocabulary type);

    List<Vocabulary> findAllbyListeType(UUID idListe, TypeVocabulary type);
    
    
}
