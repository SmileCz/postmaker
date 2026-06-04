package org.smilecz.postmaker.identity.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.api.dto.CurrentUserResponse;
import org.smilecz.postmaker.identity.domain.CurrentUserProvider;

@Controller("/api/identity/current-user")
@RequiredArgsConstructor
public class CurrentUserController {

    private final CurrentUserProvider currentUserProvider;

    @Get
    public CurrentUserResponse get() {
        var currentUser = currentUserProvider.get();
        return new CurrentUserResponse(currentUser.id(), currentUser.email(), currentUser.roles());
    }
}
