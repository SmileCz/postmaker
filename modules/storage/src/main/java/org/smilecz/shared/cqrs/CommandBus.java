package org.smilecz.shared.cqrs;

import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class CommandBus {

    private final Map<Class<?>, CommandHandler<?>> handlers;

    public CommandBus(Collection<CommandHandler<?>> handlers) {
        this.handlers = new HashMap<>();
        for (CommandHandler<?> handler : handlers) {
            CommandHandler<?> previous = this.handlers.put(handler.commandType(), handler);
            if (previous != null) {
                throw new IllegalStateException("Duplicate command handler for " + handler.commandType().getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <C extends Command> void send(C command) {
        CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No command handler registered for " + command.getClass().getName());
        }
        handler.handle(command);
    }
}
