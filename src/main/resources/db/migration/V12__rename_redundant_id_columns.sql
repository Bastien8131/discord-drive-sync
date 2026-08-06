ALTER TABLE public.label RENAME COLUMN id_label TO id;
ALTER SEQUENCE IF EXISTS public.label_id_label_seq RENAME TO label_id_seq;

ALTER TABLE public.category RENAME COLUMN id_category TO id;
ALTER SEQUENCE IF EXISTS public.category_id_category_seq RENAME TO category_id_seq;

ALTER TABLE public.link RENAME COLUMN id_link TO id;

ALTER TABLE public.discord_user RENAME COLUMN id_discord_user TO id;

ALTER TABLE public.discord_category RENAME COLUMN id_disc_category TO id;
ALTER TABLE public.category RENAME COLUMN discord_category_id_disc_category TO discord_category_id;

ALTER TABLE public.discord_channel RENAME COLUMN id_discord_channel TO id;
ALTER TABLE public.discord_channel RENAME COLUMN id_disc_category TO id_discord_category;

ALTER TABLE public.discord_message RENAME COLUMN id_discord_message TO id;
ALTER TABLE public.discord_comment RENAME COLUMN id_discord_comment TO id;
ALTER TABLE public.discord_comment RENAME COLUMN id_discord_message TO id_discord_referenced_message;

ALTER TABLE public.drive_file RENAME COLUMN id_file TO id;
ALTER TABLE public.drive_file RENAME COLUMN path TO storage_key;

ALTER TABLE public.discord_message_data RENAME COLUMN id_discord_message_data TO id;
