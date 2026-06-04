package org.smilecz.postmaker.identity.infrastructure.security;

import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.domain.CurrentUser;
import org.smilecz.postmaker.identity.domain.CurrentUserProvider;

import java.util.HashSet;

@Singleton
@RequiredArgsConstructor
public class IdentityCurrentProvider implements CurrentUserProvider {

    private final Authentication authentication;

    @Override
    public CurrentUser get() {
        var rawUserId = authentication.getAttributes().getOrDefault("sub", authentication.getName()).toString();
        var email = authentication.getAttributes().getOrDefault("email", "").toString();
        return new CurrentUser(
                Long.parseLong(rawUserId),
                email,
                new HashSet<>(authentication.getRoles()));
    }
}
