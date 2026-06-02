package org.smilecz.modules.storage.api.command;

import org.smilecz.shared.cqrs.Command;

public record CreateUserCommand(String email, String passwordHash) implements Command {
}
