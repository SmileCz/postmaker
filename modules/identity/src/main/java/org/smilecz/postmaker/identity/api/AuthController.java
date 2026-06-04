package org.smilecz.postmaker.identity.api;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.api.dto.AuthResponse;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.identity.application.command.RegisterUserCommand;
import org.smilecz.postmaker.identity.application.handler.RegisterUserHandler;

@Controller("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserHandler registerUserHandler;

    @Post("/register")
    public AuthResponse register(@Body @Valid RegisterRequest request) {
        var token = registerUserHandler.handle(new RegisterUserCommand(request.email(), request.password(), request.displayName()));
        return AuthResponse.bearer(token);
    }
}
