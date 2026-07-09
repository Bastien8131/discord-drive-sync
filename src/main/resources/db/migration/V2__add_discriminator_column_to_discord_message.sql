alter table discord_message add dtype varchar;

update discord_message dm
set dtype = 'CORE_MESSAGE'
where dm.id_discord_message in (
    select id_discord_message from message
);

update discord_message dm
set dtype = 'DISCORD_COMMENT'
where dm.id_discord_message in (
    select id_discord_message from discord_comment
);

update discord_message dm
set dtype = 'DISCORD_MESSAGE'
where dtype isnull ;

alter table discord_message alter column dtype set not null;