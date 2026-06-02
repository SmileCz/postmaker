package org.smilecz.modules.storage.api.query;

import org.smilecz.shared.cqrs.Query;

import java.util.Optional;

public record FindUserByEmailQuery(String email) implements Query<Optional<UserView>> {
}
