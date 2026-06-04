package org.smilecz.postmaker.identity.infrastructure.persistance;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.ANSI)
public interface UserRepository {
    @Query("SELECT * FROM identity_user WHERE email = :email")
    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity userEntity);
}
