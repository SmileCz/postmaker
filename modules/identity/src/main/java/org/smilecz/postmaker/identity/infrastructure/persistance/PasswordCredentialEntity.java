package org.smilecz.postmaker.identity.infrastructure.persistance;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@MappedEntity("identity_password_credential")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = false)
public class PasswordCredentialEntity {

    @Id
    @MappedProperty("user_id")
    private Long userId;

    @MappedProperty("password_hash")
    private String passwordHash;

    @MappedProperty("password_changed_at")
    private Instant passwordChangedAt;

    @DateCreated
    @MappedProperty("created_at")
    private Instant createdAt;


}
