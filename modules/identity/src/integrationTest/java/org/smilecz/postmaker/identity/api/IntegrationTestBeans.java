package org.smilecz.postmaker.identity.api;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Factory
class IntegrationTestBeans {

    @Singleton
    @Primary
    TokenGenerator tokenGenerator() {
        return new TokenGenerator() {
            @Override
            public Optional<String> generateToken(io.micronaut.security.authentication.Authentication authentication, Integer expiration) {
                return Optional.of("integration-token");
            }

            @Override
            public Optional<String> generateToken(Map<String, Object> claims) {
                return Optional.of("integration-token");
            }
        };
    }
}
