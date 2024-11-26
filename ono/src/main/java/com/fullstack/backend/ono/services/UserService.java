package com.fullstack.backend.ono.services;

import com.fullstack.backend.ono.exceptions.ConflictException;
import com.fullstack.backend.ono.exceptions.errors.UserErrorCode;
import com.fullstack.backend.ono.models.converters.UserConverter;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.dtos.UserRegistrationDto;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder encoder;

    /**
     * Register a new user
     * @param dto
     * @return
     */
    @Transactional
    public UserDto registerUser(UserRegistrationDto dto) {
        log.info("Registering user : {}", dto.getUsername());

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new ConflictException(UserErrorCode.EMAIL_ALREADY_EXISTS, dto.getEmail());
                });

        String encodedPassword = encoder.encode(dto.getPassword());


        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .build();

        return userConverter.convert(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        log.info("Getting user by id : {}", userId);

        return userRepository.findById(userId)
                .map(userConverter::convert)
                .orElseThrow(() -> new ConflictException(UserErrorCode.NOT_FOUND, userId.toString()));
    }

}
