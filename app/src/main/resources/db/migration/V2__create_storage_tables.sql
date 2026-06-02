create table stored_data (
    id integer primary key autoincrement,
    owner_user_id integer not null,
    type text not null,
    title text,
    content text not null,
    metadata_json text,
    status text not null,
    created_at datetime not null,
    updated_at datetime not null
);

create index idx_stored_data_owner_user_id on stored_data (owner_user_id);
create index idx_stored_data_type on stored_data (type);