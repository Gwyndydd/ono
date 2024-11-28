package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.Grammar;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, UUID>  {

    List<Grammar> findAllByGrammarListId(UUID idListGrammar);
    Optional<Grammar> findByNotionAndGrammarListId(String notion, UUID idListGrammar);
    
}
