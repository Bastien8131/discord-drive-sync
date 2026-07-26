alter table drive_file add column share_token varchar(32) unique;

update drive_file as df
set share_token = replace(gen_random_uuid()::text, '-', '')
where df.share_token isnull;

alter table drive_file alter column share_token set not null;