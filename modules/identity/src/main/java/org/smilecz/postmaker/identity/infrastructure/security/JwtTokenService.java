package org.smilecz.postmaker.identity.infrastructure.security;

import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserEntity;

import java.util.List;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class JwtTokenService {

    private final TokenGenerator tokenGenerator;

    public String createAccessToken(UserEntity user) {
        var claims = Map.of(
                "sub", user.getId().toString(),
                "email", user.getEmail(),
                "roles", List.of("USER")
        );

        return tokenGenerator.generateToken(claims)
                .orElseThrow(() -> new IllegalStateException("Cannot generate JWT token"));
    }
}
