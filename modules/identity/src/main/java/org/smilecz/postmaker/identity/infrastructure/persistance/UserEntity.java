package org.smilecz.postmaker.identity.infrastructure.persistance;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.smilecz.postmaker.identity.domain.UserStatus;

import java.time.Instant;

@MappedEntity("identity_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = false)
public class UserEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    private String email;

    @MappedProperty("display_name")
    private String displayName;

    private UserStatus status;

    @DateCreated
    @MappedProperty("created_at")
    private Instant createdAt;

}
