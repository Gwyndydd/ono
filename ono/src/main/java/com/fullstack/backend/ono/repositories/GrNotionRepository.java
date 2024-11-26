package com.fullstack.backend.ono.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.GrammarNotion;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrNotionRepository extends JpaRepository<GrammarNotion, UUID>  {

    Optional<GrammarNotion> findAllbyGrammar(UUID idGrammar);

}
