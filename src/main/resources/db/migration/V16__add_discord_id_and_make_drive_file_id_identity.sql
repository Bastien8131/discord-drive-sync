alter table drive_file add column discord_id bigint unique;

update drive_file as df
set discord_id = df.id
where df.discord_id isnull;

alter table drive_file alter column id add generated always as identity;
