package org.smilecz.postmaker.web.auth.client;

import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smilecz.postmaker.identity.api.dto.AuthResponse;
import org.smilecz.postmaker.identity.api.dto.LoginRequest;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IdentityAuthClientTest {

    @Mock
    private IdentityAuthClient client;

    @Test
    void register_returnsMockedResponse() {
        var request = new RegisterRequest("User@Example.com", "secret123", "Jane Doe");
        HttpResponse<?> response = HttpResponse.ok(AuthResponse.bearer("token-123"));
        doReturn(response).when(client).register(request);

        HttpResponse<?> actual = client.register(request);

        assertSame(response, actual);
        assertEquals("token-123", ((AuthResponse) actual.body()).accesToken());
        verify(client).register(request);
    }

    @Test
    void login_returnsMockedResponse() {
        var request = new LoginRequest("User@Example.com", "secret123");
        HttpResponse<?> response = HttpResponse.ok(AuthResponse.bearer("token-456"));
        doReturn(response).when(client).login(request);

        HttpResponse<?> actual = client.login(request);

        assertSame(response, actual);
        assertEquals("token-456", ((AuthResponse) actual.body()).accesToken());
        verify(client).login(request);
    }
}
