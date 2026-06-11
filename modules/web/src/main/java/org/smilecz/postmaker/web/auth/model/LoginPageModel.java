package org.smilecz.postmaker.web.auth.model;

import io.micronaut.core.annotation.Introspected;
import org.smilecz.postmaker.web.auth.form.LoginForm;


@Introspected
public record LoginPageModel(LoginForm loginForm, String error) {

    public static LoginPageModel empty() { return new LoginPageModel(new LoginForm("", ""), null);}
    public static LoginPageModel withError(LoginForm loginForm, String error) { return new LoginPageModel(loginForm, error);}

    public boolean hasError() {
        return error != null && !error.isBlank();
    }

}
