package com.fullstack.backend.ono.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgramRepository,UUID> {

    Optional<StudyProgramRepository> findAllByOwner(UUID idOwner);
    Optional<StudyProgramRepository> findByOwnerName(UUID idOwner, String name);
    
}
