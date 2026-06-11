package org.smilecz.postmaker.web.auth.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.ModelAndView;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.web.auth.client.IdentityAuthClient;
import org.smilecz.postmaker.web.auth.form.RegisterForm;
import org.smilecz.postmaker.web.auth.model.RegisterPageModel;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class RegisterPageController {

    private final IdentityAuthClient identityAuthClient;

    @Get("/register")
    public ModelAndView<RegisterPageModel> registerPage() {
        return createRegisterView(RegisterPageModel.empty());
    }

    @Post("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> register(@Body RegisterForm registerForm) {

        try {
            identityAuthClient.register(new RegisterRequest(registerForm.email(), registerForm.password(),registerForm.email()));
            return HttpResponse.seeOther(URI.create("/login"));
        }
        catch (HttpClientResponseException ex) {
            var errorMessage = resolveRegisterErrorMessage(ex);
            var registerView = createRegisterView(RegisterPageModel.withError(registerForm,errorMessage));
            return HttpResponse.ok(registerView);
        }

    }

    private ModelAndView<RegisterPageModel> createRegisterView(RegisterPageModel registerPageModel) {
        return new ModelAndView<>("auth/register", registerPageModel);
    }

    private String resolveRegisterErrorMessage(HttpClientResponseException ex) {
        if (ex.getStatus() == HttpStatus.CONFLICT) {
            return "User with this email already exists.";
        }
        return "An unexpected error occurred. Please try again.";
    }

}
