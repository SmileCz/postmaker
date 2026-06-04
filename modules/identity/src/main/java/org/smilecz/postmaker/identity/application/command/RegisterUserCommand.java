package org.smilecz.postmaker.identity.application.command;

public record RegisterUserCommand(String email, String password, String displayName ) {
}
