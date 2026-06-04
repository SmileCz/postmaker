package org.smilecz.postmaker.identity.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smilecz.postmaker.identity.api.dto.AuthResponse;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.identity.application.command.RegisterUserCommand;
import org.smilecz.postmaker.identity.application.handler.RegisterUserHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private RegisterUserHandler registerUserHandler;

    @InjectMocks
    private AuthController controller;

    @Test
    void register_forwardsRequestToHandlerAndReturnsBearerToken() {
        when(registerUserHandler.handle(any())).thenReturn("token-123");
        var request = new RegisterRequest("User@Example.com", "secret123", "Jane Doe");

        AuthResponse response = controller.register(request);

        var commandCaptor = ArgumentCaptor.forClass(RegisterUserCommand.class);
        verify(registerUserHandler).handle(commandCaptor.capture());

        assertEquals(new RegisterUserCommand("User@Example.com", "secret123", "Jane Doe"), commandCaptor.getValue());
        assertEquals("token-123", response.accesToken());
        assertEquals("Bearer", response.tokenType());
    }

    @Test
    void register_propagatesHandlerExceptions() {
        when(registerUserHandler.handle(any())).thenThrow(new IllegalArgumentException("invalid request"));
        var request = new RegisterRequest("User@Example.com", "secret123", "Jane Doe");

        var exception = assertThrows(IllegalArgumentException.class, () -> controller.register(request));

        assertEquals("invalid request", exception.getMessage());
    }
}
