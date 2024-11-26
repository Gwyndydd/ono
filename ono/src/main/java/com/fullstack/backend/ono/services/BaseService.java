package com.fullstack.backend.ono.services;

import com.fullstack.backend.ono.models.auth.CustomPrincipal;
import com.fullstack.backend.ono.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public interface BaseService {

    default Optional<CustomPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof CustomPrincipal user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
