package org.smilecz.postmaker.web.auth.form;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record RegisterForm(String email, String password, String confirmPassword) { }
