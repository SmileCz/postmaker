package org.smilecz.postmaker.identity.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.smile.cz.postmaker.shared.result.ErrorType;
import org.smile.cz.postmaker.shared.result.Result;
import org.smilecz.postmaker.identity.api.dto.AuthResponse;
import org.smilecz.postmaker.identity.api.dto.LoginRequest;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.identity.application.command.LoginWithPasswordCommand;
import org.smilecz.postmaker.identity.application.command.RegisterUserCommand;
import org.smilecz.postmaker.identity.application.handler.LoginWithPasswordHandler;
import org.smilecz.postmaker.identity.application.handler.RegisterUserHandler;

@Controller("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserHandler registerUserHandler;
    private final LoginWithPasswordHandler loginWithPasswordHandler;

    @Post("/register")
    public HttpResponse<?> register(@Body @Valid RegisterRequest request) {
        var result = registerUserHandler.handle(new RegisterUserCommand(request.email(), request.password(), request.displayName()));

        if (result.isSuccess()) {
            var okResult = (Result.Success<String>) result;
            return HttpResponse.ok(AuthResponse.bearer(okResult.value()));
        }

        var failResult = (Result.Failure<String>) result;

        if (failResult.errorCode().type() == ErrorType.CONFLICT) {
            return HttpResponseFactory.INSTANCE.status(HttpStatus.CONFLICT, failResult.errorCode().message());
        }

        return HttpResponse.badRequest(failResult.errorCode().code());

    }

    @Post("/login")
    public HttpResponse<?> login(@Body @Valid LoginRequest request) {
        var result = loginWithPasswordHandler.handle(new LoginWithPasswordCommand(request.email(), request.password()));

        if (result.isSuccess()) {
            var okResult = (Result.Success<String>) result;
            return HttpResponse.ok(AuthResponse.bearer(okResult.value()));
        }

        var failResult = (Result.Failure<String>) result;

        if (failResult.errorCode().type() == ErrorType.VALIDATION) {
            return HttpResponse.unauthorized();
        }

        return HttpResponse.badRequest(failResult.errorCode().code());
    }
}
