package org.smilecz.shared.cqrs;

public interface CommandHandler<C extends Command> {

    Class<C> commandType();

    void handle(C command);
}
