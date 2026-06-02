package org.smilecz.modules.storage.application.command;

import jakarta.inject.Singleton;
import org.smilecz.modules.storage.api.command.CreateUserCommand;
import org.smilecz.modules.storage.domain.UserAccount;
import org.smilecz.modules.storage.domain.UserRepository;
import org.smilecz.shared.cqrs.CommandHandler;

import java.util.UUID;

@Singleton
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

    private final UserRepository userRepository;

    public CreateUserCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Class<CreateUserCommand> commandType() {
        return CreateUserCommand.class;
    }

    @Override
    public void handle(CreateUserCommand command) {
        userRepository.findByEmail(command.email()).ifPresent(existingUser -> {
            throw new IllegalStateException("User already exists for email " + command.email());
        });

        userRepository.save(new UserAccount(
                UUID.randomUUID().toString(),
                command.email(),
                command.passwordHash()
        ));
    }
}
