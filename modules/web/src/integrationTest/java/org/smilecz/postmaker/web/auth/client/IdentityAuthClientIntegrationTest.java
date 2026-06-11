package org.smilecz.postmaker.web.auth.client;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.smilecz.postmaker.identity.api.dto.AuthResponse;
import org.smilecz.postmaker.identity.api.dto.LoginRequest;
import org.smilecz.postmaker.identity.api.dto.RegisterRequest;
import org.smilecz.postmaker.identity.domain.UserStatus;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserEntity;
import org.smilecz.postmaker.identity.infrastructure.persistance.UserRepository;
import org.smilecz.postmaker.identity.infrastructure.security.PasswordHasher;

import java.sql.DriverManager;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IdentityAuthClientIntegrationTest {

    private static final String JDBC_URL = "jdbc:sqlite:file:identity-it?mode=memory&cache=shared&foreign_keys=on";

    private EmbeddedServer embeddedServer;
    private IdentityAuthClient client;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    @BeforeAll
    @SneakyThrows
    void startServer() {
        embeddedServer = ApplicationContext.run(
                EmbeddedServer.class,
                Map.of(
                        "datasources.default.url", JDBC_URL,
                        "datasources.default.driver-class-name", "org.sqlite.JDBC",
                        "datasources.default.db-type", "sqlite",
                        "micronaut.security.enabled", false,
                        "micronaut.security.token.jwt.signatures.secret.generator.secret", "integration-test-secret-integration-test-secret"
                ),
                "test"
        );

        HttpClient httpClient = HttpClient.create(embeddedServer.getURL());
        client = new IdentityAuthClient() {
            @Override
            public HttpResponse<?> login(LoginRequest request) {
                return httpClient.toBlocking().exchange(HttpRequest.POST("/api/auth/login", request), AuthResponse.class);
            }

            @Override
            public HttpResponse<?> register(RegisterRequest request) {
                return httpClient.toBlocking().exchange(HttpRequest.POST("/api/auth/register", request), AuthResponse.class);
            }
        };

        userRepository = embeddedServer.getApplicationContext().getBean(UserRepository.class);
        passwordHasher = embeddedServer.getApplicationContext().getBean(PasswordHasher.class);
    }

    @AfterAll
    void stopServer() {
        if (embeddedServer != null) {
            embeddedServer.close();
        }
    }

    @BeforeEach
    @SneakyThrows
    void setUpSchema() {
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
        var httpResponse = client.register(new RegisterRequest("User@Example.com", "secret123", "Jane Doe"));

        assertEquals(HttpStatus.OK, httpResponse.getStatus());
        assertInstanceOf(AuthResponse.class, httpResponse.body());

        AuthResponse response = (AuthResponse) httpResponse.body();
        assertEquals("Bearer", response.tokenType());
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
        client.register(new RegisterRequest("User@Example.com", "secret123", "Jane Doe"));

        var exception = assertThrows(HttpClientResponseException.class,
                () -> client.register(new RegisterRequest("user@example.com", "another123", "Jane Two")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void login_returnsBearerTokenForPersistedUser() {
        client.register(new RegisterRequest("User@Example.com", "secret123", "Jane Doe"));

        var httpResponse = client.login(new LoginRequest("user@example.com", "secret123"));

        assertEquals(HttpStatus.OK, httpResponse.getStatus());
        assertInstanceOf(AuthResponse.class, httpResponse.body());

        AuthResponse response = (AuthResponse) httpResponse.body();
        assertEquals("Bearer", response.tokenType());
        assertEquals("integration-token", response.accesToken());
    }
}
