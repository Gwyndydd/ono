package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.GrammarList;

@Repository
public interface GrammarListRepository extends JpaRepository<GrammarList,UUID>{

    List<GrammarList> findAllByOwner(UUID idOwner);
    Optional<GrammarList> findByOwnerName(UUID idUser,String name);
    Optional<GrammarList> findByStudyProgramm(UUID idStudyProgramm);
    
}
