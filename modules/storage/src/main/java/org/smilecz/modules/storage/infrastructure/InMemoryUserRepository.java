package org.smilecz.modules.storage.infrastructure;

import jakarta.inject.Singleton;
import org.smilecz.modules.storage.domain.UserAccount;
import org.smilecz.modules.storage.domain.UserRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentMap<String, UserAccount> usersByEmail = new ConcurrentHashMap<>();

    @Override
    public UserAccount save(UserAccount userAccount) {
        usersByEmail.put(userAccount.email(), userAccount);
        return userAccount;
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }
}
