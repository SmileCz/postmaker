package org.smilecz.modules.storage.application.query;

import jakarta.inject.Singleton;
import org.smilecz.modules.storage.api.query.FindUserByEmailQuery;
import org.smilecz.modules.storage.api.query.UserView;
import org.smilecz.modules.storage.domain.UserRepository;
import org.smilecz.shared.cqrs.QueryHandler;

import java.util.Optional;

@Singleton
public class FindUserByEmailQueryHandler implements QueryHandler<FindUserByEmailQuery, Optional<UserView>> {

    private final UserRepository userRepository;

    public FindUserByEmailQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Class<FindUserByEmailQuery> queryType() {
        return FindUserByEmailQuery.class;
    }

    @Override
    public Optional<UserView> handle(FindUserByEmailQuery query) {
        return userRepository.findByEmail(query.email())
                .map(user -> new UserView(user.id(), user.email(), user.passwordHash()));
    }
}
