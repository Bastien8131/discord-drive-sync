alter table discord_message_data
    add column id_discord_user bigint constraint fk_discord_message_data_discord_user references discord_user;

create table repost(
    id_discord_user bigint not null constraint fk_repost_discord_user references discord_user,
    id_discord_message_data bigint not null constraint fk_contain_discord_message_data references discord_message_data,
    primary key (id_discord_user, id_discord_message_data)
)