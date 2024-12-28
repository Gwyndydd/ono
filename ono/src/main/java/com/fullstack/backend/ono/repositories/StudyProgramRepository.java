package com.fullstack.backend.ono.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.backend.ono.models.entities.StudyProgram;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram,UUID> {

    //@Query(nativeQuery = true, value = "SELECT * FROM study-programs WHERE owner = ?1")
    List<StudyProgram> findAllByOwnerId(UUID idOwner);

    //@Query(nativeQuery = true, value = "SELECT * FROM study-programs WHERE name= ?2 owner = ?1")
    Optional<StudyProgram> findByOwnerIdAndName(UUID idOwner, String name);

    List<StudyProgram> findAllByVisibility(boolean visibility);
    
}
