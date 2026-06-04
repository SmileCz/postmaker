package org.smilecz.postmaker.identity.infrastructure.persistance;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.ANSI)
public interface PasswordCredentialRepository {

    PasswordCredentialEntity save(PasswordCredentialEntity passwordCredentialEntity);
}
