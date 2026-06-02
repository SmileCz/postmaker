create table identity_user (
    id integer primary key autoincrement,
    email text not null unique,
    display_name text not null,
    status text not null,
    created_at timestamp not null
);

create table identity_password_credential (
    user_id integer primary key,
    password_hash text not null,
    password_changed_at timestamp not null,
    created_at timestamp not null,
    constraint fk_identity_password_user
        foreign key (user_id)
        references identity_user (id)
        on delete cascade
);

create table identity_external_account (
    id integer primary key,
    user_id integer not null,
    provider text not null,
    provider_subject text not null,
    email text not null,
    email_verified boolean not null,
    created_at timestamp not null,
    constraint fk_identity_external_user
        foreign key (user_id)
        references identity_user (id)
        on delete cascade,
    constraint uq_identity_external_provider_subject
        unique (provider, provider_subject)

);

create table identity_refresh_token (
    id integer primary key,
    user_id integer not null,
    token_hash text not null unique,
    expires_at timestamp not null,
    rewoked_at timestamp,
    created_at timestamp not null,
    constraint fk_identity_refresh_token_user
        foreign key (user_id)
        references identity_user (id)
        on delete cascade
);