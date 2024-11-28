package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.StudyProgram;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram,UUID> {

    List<StudyProgram> findAllByOwner(UUID idOwner);
    Optional<StudyProgram> findByOwnerName(UUID idOwner, String name);
    
}
