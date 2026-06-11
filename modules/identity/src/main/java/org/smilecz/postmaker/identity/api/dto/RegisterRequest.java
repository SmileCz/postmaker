package org.smilecz.postmaker.identity.api.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max=128) String password,
        @NotBlank @Size(max = 255) String displayName
        ) {
}
