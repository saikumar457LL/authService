
alter table users add column uuid uuid not null default gen_random_uuid();

alter table users
    add constraint users_uuid_unique unique (uuid);