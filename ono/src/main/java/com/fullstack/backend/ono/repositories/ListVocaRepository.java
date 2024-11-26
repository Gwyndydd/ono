package com.fullstack.backend.ono.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.ListVocabulary;

@Repository
public interface ListVocaRepository extends JpaRepository<ListVocabulary,UUID> {

    Optional<ListVocabulary> findAllbyOwner(UUID idUser);
    Optional<ListVocabulary> findByOwnerName(UUID idUser,String name);
    Optional<ListVocabulary> findByStudyProgramm(UUID idStudyProgramm);
}
