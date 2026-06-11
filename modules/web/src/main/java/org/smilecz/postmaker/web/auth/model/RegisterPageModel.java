package org.smilecz.postmaker.web.auth.model;

import io.micronaut.core.annotation.Introspected;
import org.smilecz.postmaker.web.auth.form.RegisterForm;

@Introspected
public record RegisterPageModel(RegisterForm registerForm, String error) {

    public static RegisterPageModel empty() { return new RegisterPageModel(new RegisterForm("","",""), null);}
    public static RegisterPageModel withError(RegisterForm registerForm, String withError) { return new RegisterPageModel(registerForm, withError);}
    public boolean hasError() { return error != null && !error.isBlank(); }

}
