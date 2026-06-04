package org.smilecz.postmaker.identity.api.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AuthResponse(String accesToken,String tokenType) {

    public static AuthResponse bearer(String accessToken) {
        return new AuthResponse(accessToken, "Bearer");
    }
}
