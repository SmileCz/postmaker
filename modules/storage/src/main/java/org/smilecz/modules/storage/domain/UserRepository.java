package org.smilecz.modules.storage.domain;

import java.util.Optional;

public interface UserRepository {

    UserAccount save(UserAccount userAccount);

    Optional<UserAccount> findByEmail(String email);
}
