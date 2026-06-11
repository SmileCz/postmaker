package org.smilecz.postmaker.identity.application.handler;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.smile.cz.postmaker.shared.result.ErrorCode;
import org.smile.cz.postmaker.shared.result.Result;
import org.smilecz.postmaker.identity.application.command.LoginWithPasswordCommand;
import org.smilecz.postmaker.identity.domain.UserStatus;
import org.smilecz.postmaker.identity.infrastructure.persistance.PasswordCredentialRepository;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserRepository;
import org.smilecz.postmaker.identity.infrastructure.security.JwtTokenService;
import org.smilecz.postmaker.identity.infrastructure.security.PasswordHasher;

import java.util.Locale;

@Singleton
@RequiredArgsConstructor
public class LoginWithPasswordHandler {

    private final UserRepository userRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PasswordHasher passwordHasher;
    private final JwtTokenService jwtTokenService;

    public Result<String> handle(LoginWithPasswordCommand command) {
        var normalizedEmail = command.email().toLowerCase(Locale.ROOT).trim();
        var userOptional =  userRepository.findByEmail(normalizedEmail);

        if (userOptional.isEmpty()) {
            return Result.failure(ErrorCode.INVALID_CREDENTIALS);
        }

        var user = userOptional.get();

        if (UserStatus.PENDING == user.getStatus()) {
            return Result.failure(ErrorCode.USER_NOT_ACTIVE);
        }

        if (UserStatus.ACTIVE != user.getStatus()) {
            return Result.failure(ErrorCode.INVALID_CREDENTIALS);
        }



        var credentialOptional =  passwordCredentialRepository.findById(user.getId());

        if (credentialOptional.isEmpty()) {
            return Result.failure(ErrorCode.INVALID_CREDENTIALS);
        }
        var credential = credentialOptional.get();

        if (!passwordHasher.matches(command.password(), credential.getPasswordHash())) {
            return Result.failure(ErrorCode.INVALID_CREDENTIALS);
        }

        var token = jwtTokenService.createAccessToken(user);
        return Result.success(token);
    }
}
