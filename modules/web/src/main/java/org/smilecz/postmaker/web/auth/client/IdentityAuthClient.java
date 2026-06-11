package org.smilecz.postmaker.web.auth.client;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.smilecz.postmaker.identity.api.dto.LoginRequest;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;

@Client("/api/auth")
public interface IdentityAuthClient {

    @Post("/login")
    HttpResponse<?> login(@Body LoginRequest request);

    @Post("/register")
    HttpResponse<?> register(@Body RegisterRequest request);
}
