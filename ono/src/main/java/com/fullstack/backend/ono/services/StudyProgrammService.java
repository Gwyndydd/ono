package com.fullstack.backend.ono.services;

import org.springframework.stereotype.Service;

import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyProgrammService {

    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgramConverter studyProgramConverter;
    private final UserRepository userRepository;
    
    
}
