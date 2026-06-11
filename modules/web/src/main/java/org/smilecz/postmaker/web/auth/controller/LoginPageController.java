package org.smilecz.postmaker.web.auth.controller;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.views.ModelAndView;
import lombok.RequiredArgsConstructor;
import org.smilecz.postmaker.identity.api.dto.LoginRequest;
import org.smilecz.postmaker.web.auth.client.IdentityAuthClient;
import org.smilecz.postmaker.web.auth.form.LoginForm;
import org.smilecz.postmaker.web.auth.model.LoginPageModel;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class LoginPageController {

    private final IdentityAuthClient identityAuthClient;

    @Get("/login")
    public ModelAndView<LoginPageModel> loginPage() {
        return createLoginView(LoginPageModel.empty());
    }

    @Post("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> login(@Body LoginForm loginForm) {

        try {
            identityAuthClient.login(new LoginRequest(loginForm.email(), loginForm.password()));
            return HttpResponse.seeOther(URI.create("/"));
        }
        catch (HttpClientResponseException ex) {

            var errorMessage = resolveLoginErrorMessage(ex);
            var loginView = createLoginView(LoginPageModel.withError(loginForm,errorMessage));
            System.out.println("ERROR MODEL: hasError=" + loginView.getModel().toString());
            return HttpResponse.ok(loginView);
        }

    }

    private ModelAndView<LoginPageModel> createLoginView(LoginPageModel loginPageModel) {
        return new ModelAndView<>("auth/login", loginPageModel);
    }

    private String resolveLoginErrorMessage(HttpClientResponseException exception) {
        if (exception.getStatus() == HttpStatus.UNAUTHORIZED) {
            return "Invalid email or password.";
        }
        return "An unexpected error occurred. Please try again.";
    }

}
