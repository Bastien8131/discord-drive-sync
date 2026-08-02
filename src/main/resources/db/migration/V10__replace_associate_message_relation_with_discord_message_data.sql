ALTER TABLE public.associate ADD COLUMN id_discord_message_data bigint;

UPDATE public.associate a
SET id_discord_message_data = dm.id_discord_message_data
FROM public.discord_message dm
WHERE dm.id_discord_message = a.id_discord_message;

DELETE FROM public.associate a1
USING public.associate a2
WHERE a1.id_label = a2.id_label
  AND a1.id_discord_message_data = a2.id_discord_message_data
  AND a1.id_discord_message > a2.id_discord_message;

ALTER TABLE public.associate DROP CONSTRAINT fkmt3lu85tj7ypnn8n0i9d05x1s;
ALTER TABLE public.associate DROP COLUMN id_discord_message;

ALTER TABLE public.associate ALTER COLUMN id_discord_message_data SET NOT NULL;

ALTER TABLE public.associate
    ADD CONSTRAINT fk_associate_discord_message_data FOREIGN KEY (id_discord_message_data) REFERENCES public.discord_message_data(id_discord_message_data);

DROP TABLE public.message;

UPDATE public.discord_message
SET dtype = 'DISCORD_MESSAGE'
WHERE dtype = 'CORE_MESSAGE';
