package org.smilecz.postmaker.web.auth.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.smilecz.postmaker.web.auth.form.LoginForm;


@Introspected
@Serdeable
public record LoginPageModel(LoginForm loginForm, String error) {

    public static LoginPageModel empty() { return new LoginPageModel(new LoginForm("", ""), null);}
    public static LoginPageModel withError(LoginForm loginForm, String error) { return new LoginPageModel(loginForm, error);}



}
