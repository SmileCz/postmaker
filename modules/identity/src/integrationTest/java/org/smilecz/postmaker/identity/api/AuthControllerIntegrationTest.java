package org.smilecz.postmaker.identity.api;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.identity.application.handler.RegisterUserHandler;
import org.smilecz.postmaker.identity.domain.UserStatus;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserEntity;
import org.smilecz.postmaker.identity.infrastructure.persistance.PasswordCredentialRepository;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserRepository;
import org.smilecz.postmaker.identity.infrastructure.security.JwtTokenService;
import org.smilecz.postmaker.identity.infrastructure.security.PasswordHasher;
import io.micronaut.security.token.generator.TokenGenerator;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(propertySources = "classpath:identity-it.properties", transactional = false)
class AuthControllerIntegrationTest {

    @Inject
    PasswordCredentialRepository passwordCredentialRepository;

    @Inject
    TokenGenerator tokenGenerator;

    private AuthController controller;

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHasher passwordHasher;

    private static final String JDBC_URL = "jdbc:sqlite:file:identity-it?mode=memory&cache=shared&foreign_keys=on";

    @BeforeEach
    @SneakyThrows
    void setUpSchema() {
        controller = new AuthController(new RegisterUserHandler(
                userRepository,
                passwordCredentialRepository,
                passwordHasher,
                new JwtTokenService(tokenGenerator)
        ));
        try (var connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             var statement = connection.createStatement()) {
            statement.executeUpdate("drop table if exists identity_password_credential");
            statement.executeUpdate("drop table if exists identity_user");

            statement.executeUpdate("""
                create table identity_user (
                    id integer primary key autoincrement,
                    email varchar(255) not null unique,
                    display_name varchar(255) not null,
                    status varchar(50) not null,
                    created_at timestamp not null
                )
                """);

            statement.executeUpdate("""
                create table identity_password_credential (
                    user_id integer primary key,
                    password_hash varchar(255) not null,
                    password_changed_at timestamp not null,
                    created_at timestamp not null,
                    constraint fk_identity_password_user
                        foreign key (user_id)
                        references identity_user (id)
                        on delete cascade
                )
                """);
        }
    }

    @Test
    @SneakyThrows
    void register_persistsUserAndPasswordCredential() {
        var response = controller.register(new RegisterRequest("User@Example.com", "secret123", "Jane Doe"));

        assertEquals("Bearer", response.tokenType());
        assertNotNull(response.accesToken());
        assertEquals("integration-token", response.accesToken());

        UserEntity user = userRepository.findByEmail("user@example.com").orElseThrow();
        assertEquals("user@example.com", user.getEmail());
        assertEquals("Jane Doe", user.getDisplayName());
        assertEquals(UserStatus.ACTIVE, user.getStatus());

        try (var connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery("""
                 select user_id, password_hash, password_changed_at, created_at
                 from identity_password_credential
                 where user_id = %d
                 """.formatted(user.getId()))) {
            assertTrue(resultSet.next());

            assertEquals(user.getId(), resultSet.getLong("user_id"));
            assertTrue(passwordHasher.matches("secret123", resultSet.getString("password_hash")));
            assertNotNull(resultSet.getTimestamp("password_changed_at"));
            assertNotNull(resultSet.getTimestamp("created_at"));
        }
    }

    @Test
    void register_rejectsDuplicateEmailAfterNormalization() {
        controller.register(new RegisterRequest("User@Example.com", "secret123", "Jane Doe"));

        var exception = assertThrows(IllegalArgumentException.class,
                () -> controller.register(new RegisterRequest("user@example.com", "another123", "Jane Two")));

        assertEquals("User with email user@example.com already exists", exception.getMessage());
    }
}
