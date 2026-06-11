package org.smilecz.postmaker.identity.api.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record LoginRequest(String email, String password) { }
