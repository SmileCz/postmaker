package org.smilecz.modules.storage.domain;

public record UserAccount(String id, String email, String passwordHash) {
}
