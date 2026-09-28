create table upload_link (
    id bigint generated always as identity constraint upload_link_pkey primary key,
    token text not null unique,
    used boolean not null default false,
    created_at timestamp with time zone not null,
    ended_at timestamp with time zone not null,
    id_discord_user bigint not null constraint fk_upload_link_discord_user references discord_user,
    id_discord_channel bigint not null constraint fk_upload_link_discord_channel references discord_channel
)
