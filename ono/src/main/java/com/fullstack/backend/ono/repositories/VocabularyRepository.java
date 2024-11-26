package com.fullstack.backend.ono.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.constants.TypeVocabulary;
import com.fullstack.backend.ono.models.entities.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary,UUID> {

    Optional<Vocabulary> findAllbyIdListe(UUID idListe);

    Optional<Vocabulary> findAllbyType(TypeVocabulary type);

    Optional<Vocabulary> findAllbyListeType(UUID idListe, TypeVocabulary type);

    Optional<Vocabulary> findAllbyOwner(UUID idUser);

    
    
}
