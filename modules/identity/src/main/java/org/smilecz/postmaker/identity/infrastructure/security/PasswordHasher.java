package org.smilecz.postmaker.identity.infrastructure.security;

public interface PasswordHasher {

    String hash(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);

}
