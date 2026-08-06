ALTER TABLE public.discord_message_data RENAME TO core_content;
ALTER TABLE public.core_content RENAME COLUMN content TO text;
ALTER SEQUENCE IF EXISTS public.discord_message_data_id_discord_message_data_seq RENAME TO core_content_id_seq;

ALTER TABLE public.discord_message RENAME COLUMN id_discord_message_data TO id_core_content;

ALTER TABLE public.repost RENAME COLUMN id_discord_message_data TO id_core_content;
ALTER TABLE public.repost RENAME CONSTRAINT fk_contain_discord_message_data TO fk_contain_core_content;

ALTER TABLE public.contain RENAME COLUMN id_discord_message_data TO id_core_content;
ALTER TABLE public.contain RENAME CONSTRAINT fk_contain_discord_message_data TO fk_contain_core_content;

ALTER TABLE public.associate RENAME COLUMN id_discord_message_data TO id_core_content;
ALTER TABLE public.associate RENAME CONSTRAINT fk_associate_discord_message_data TO fk_associate_core_content;

ALTER TABLE public.description RENAME COLUMN id_discord_message_data TO id_core_content;
ALTER TABLE public.description RENAME CONSTRAINT fk_description_discord_message_data TO fk_description_core_content;

ALTER TABLE public.core_content RENAME CONSTRAINT fk_discord_message_data_discord_user TO fk_core_content_discord_user;
