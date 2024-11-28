package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.VocabularyList;

@Repository
public interface VocaListRepository extends JpaRepository<VocabularyList,UUID> {

    List<VocabularyList> findAllbyOwner(UUID idUser);
    Optional<VocabularyList> findByOwnerName(UUID idUser,String name);
    Optional<VocabularyList> findByStudyProgramm(UUID idStudyProgramm);
}
