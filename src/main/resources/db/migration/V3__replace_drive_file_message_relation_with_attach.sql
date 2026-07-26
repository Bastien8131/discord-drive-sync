ALTER TABLE public.drive_file DROP CONSTRAINT fkl736v7bea5dcld11wexp9hyca;
ALTER TABLE public.drive_file DROP COLUMN id_discord_message;

CREATE TABLE public.attach (
    id_file bigint NOT NULL,
    id_discord_message bigint NOT NULL,
    CONSTRAINT attach_pkey PRIMARY KEY (id_file, id_discord_message),
    CONSTRAINT fk_attach_file FOREIGN KEY (id_file) REFERENCES public.drive_file(id_file),
    CONSTRAINT fk_attach_message FOREIGN KEY (id_discord_message) REFERENCES public.discord_message(id_discord_message)
);
