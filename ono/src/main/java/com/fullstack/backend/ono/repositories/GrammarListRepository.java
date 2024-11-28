package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.GrammarList;

@Repository
public interface GrammarListRepository extends JpaRepository<GrammarList,UUID>{

    //@Query(nativeQuery = true, value = "SELECT * FROM liste_grammaire WHERE owner = ?1")
    List<GrammarList> findAllByOwnerId(UUID owner);

    //@Query(nativeQuery = true, value = "SELECT * FROM liste_grammaire WHERE name= ?1 owner = ?2")
    Optional<GrammarList> findByNameAndOwnerId(String name, UUID idOwner);

    //@Query(nativeQuery = true, value = "SELECT * FROM liste_grammaire WHERE programme_etude = ?1")
    Optional<GrammarList> findByStudyProgramId(UUID idStudyProgramm);
    
}
