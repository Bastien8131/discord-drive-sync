DROP TABLE public.attach;

ALTER TABLE public.message DROP CONSTRAINT fk8xacwmdggnvkltgti4q821e8v;
ALTER TABLE public.message DROP CONSTRAINT ukfob45yvao8lij0xrietifqgdr;
ALTER TABLE public.message DROP COLUMN id_file;

CREATE TABLE public.description (
    id_discord_message bigint NOT NULL,
    id_file bigint NOT NULL,
    CONSTRAINT description_pkey PRIMARY KEY (id_discord_message, id_file),
    CONSTRAINT fk_description_message FOREIGN KEY (id_discord_message) REFERENCES public.discord_message(id_discord_message),
    CONSTRAINT fk_description_file FOREIGN KEY (id_file) REFERENCES public.drive_file(id_file)
);
