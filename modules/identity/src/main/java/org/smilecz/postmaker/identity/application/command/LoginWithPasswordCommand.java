package org.smilecz.postmaker.identity.application.command;

public record LoginWithPasswordCommand(String email, String password) { }
