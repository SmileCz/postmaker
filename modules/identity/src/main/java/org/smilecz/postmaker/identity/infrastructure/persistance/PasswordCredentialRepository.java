package org.smilecz.postmaker.identity.infrastructure.persistance;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.ANSI)
public interface PasswordCredentialRepository extends CrudRepository<PasswordCredentialEntity, Long> {

}
