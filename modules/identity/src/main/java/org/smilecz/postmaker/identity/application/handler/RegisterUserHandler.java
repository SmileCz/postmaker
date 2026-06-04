package org.smilecz.postmaker.identity.application.handler;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.application.command.RegisterUserCommand;
import org.smilecz.postmaker.identity.domain.UserStatus;
import org.smilecz.postmaker.identity.infrastructure.persistance.PasswordCredentialEntity;
import org.smilecz.postmaker.identity.infrastructure.persistance.PasswordCredentialRepository;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserEntity;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserRepository;
import org.smilecz.postmaker.identity.infrastructure.security.JwtTokenService;
import org.smilecz.postmaker.identity.infrastructure.security.PasswordHasher;

import java.time.Instant;
import java.util.Locale;

@Singleton
@RequiredArgsConstructor
public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PasswordHasher passwordHasher;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public String handle(RegisterUserCommand command) {
        var normalizedEmail = command.email().toLowerCase(Locale.ROOT).trim();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("User with email " + normalizedEmail + " already exists");
        }

        var userEntity = new UserEntity();
        userEntity.setEmail(normalizedEmail);
        userEntity.setDisplayName(command.displayName());
        userEntity.setStatus(UserStatus.ACTIVE);
        var savedUserEntity = userRepository.save(userEntity);

        var credential = new PasswordCredentialEntity();
        credential.setUserId(savedUserEntity.getId());
        credential.setPasswordHash(passwordHasher.hash(command.password()));
        credential.setPasswordChangedAt(Instant.now());
        passwordCredentialRepository.save(credential);
        return jwtTokenService.createAccessToken(savedUserEntity);
    }
}
