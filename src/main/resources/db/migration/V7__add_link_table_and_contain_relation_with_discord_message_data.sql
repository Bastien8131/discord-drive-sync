create table link (
    id_link bigint constraint link_pkey primary key,
    url text not null unique
);

create table contain (
    id_link bigint not null constraint fk_contain_link references link,
    id_discord_message_data bigint not null constraint fk_contain_discord_message_data references discord_message_data,
    primary key (id_link, id_discord_message_data)
)