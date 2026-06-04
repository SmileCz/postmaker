alter table identity_refresh_token
    rename column rewoked_at to revoked_at;

create index if not exists idx_identity_password_credential_user_id
    on identity_password_credential(user_id);

create index if not exists idx_identity_external_account_user_id
    on identity_external_account(user_id);

create index if not exists idx_identity_refresh_token_user_id
    on identity_refresh_token(user_id);

create index if not exists idx_identity_refresh_token_user_expires_at
    on identity_refresh_token(user_id, expires_at);