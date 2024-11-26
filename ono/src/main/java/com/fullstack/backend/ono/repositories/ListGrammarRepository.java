package com.fullstack.backend.ono.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.ListGrammar;

@Repository
public interface ListGrammarRepository extends JpaRepository<ListGrammar,UUID>{

    Optional<ListGrammar> findAllByOwner(UUID idOwner);
    Optional<ListGrammar> findByOwnerName(UUID idUser,String name);
    Optional<ListGrammar> findByStudyProgramm(UUID idStudyProgramm);
    
}
