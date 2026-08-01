ALTER TABLE public.description ADD COLUMN id_discord_message_data bigint;

UPDATE public.description d
SET id_discord_message_data = dm.id_discord_message_data
FROM public.discord_message dm
WHERE dm.id_discord_message = d.id_discord_message;

DELETE FROM public.description d1
USING public.description d2
WHERE d1.id_discord_message_data = d2.id_discord_message_data
  AND d1.id_file = d2.id_file
  AND d1.id_discord_message > d2.id_discord_message;

ALTER TABLE public.description DROP CONSTRAINT fk_description_message;
ALTER TABLE public.description DROP CONSTRAINT description_pkey;
ALTER TABLE public.description DROP COLUMN id_discord_message;

ALTER TABLE public.description ALTER COLUMN id_discord_message_data SET NOT NULL;

ALTER TABLE public.description
    ADD CONSTRAINT description_pkey PRIMARY KEY (id_discord_message_data, id_file);

ALTER TABLE public.description
    ADD CONSTRAINT fk_description_discord_message_data FOREIGN KEY (id_discord_message_data) REFERENCES public.discord_message_data(id_discord_message_data);
