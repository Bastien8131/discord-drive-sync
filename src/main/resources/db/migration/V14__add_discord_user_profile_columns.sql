ALTER TABLE public.discord_user
    ADD COLUMN global_name character varying(255),
    ADD COLUMN effective_name character varying(255),
    ADD COLUMN nickname character varying(255),
    ADD COLUMN avatar_url character varying(255),
    ADD COLUMN account_created_at timestamp with time zone,
    ADD COLUMN joined_at timestamp with time zone,
    ADD COLUMN color_raw integer NOT NULL DEFAULT 0,
    ADD COLUMN is_bot boolean NOT NULL DEFAULT false,
    ADD COLUMN is_owner boolean NOT NULL DEFAULT false,
    ADD COLUMN is_pending boolean NOT NULL DEFAULT false;
