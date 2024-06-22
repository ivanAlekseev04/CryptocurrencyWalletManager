BEGIN;

CREATE TABLE IF NOT EXISTS public.crypto
(
    crypto_id bigserial NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    price double precision NOT NULL,
    CONSTRAINT crypto_pkey PRIMARY KEY (crypto_id)
);

CREATE TABLE IF NOT EXISTS public.shedlock
(
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    locked_until timestamp(3) without time zone,
    locked_at timestamp(3) without time zone,
    locked_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS public.transaction
(
    transaction_id bigserial NOT NULL,
    amount double precision NOT NULL,
    date_of_commit timestamp(6) without time zone NOT NULL,
    selling_profit double precision,
    type character varying(6) COLLATE pg_catalog."default" NOT NULL,
    crypto_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT transaction_pkey PRIMARY KEY (transaction_id)
);

CREATE TABLE IF NOT EXISTS public."user"
(
    user_id bigserial NOT NULL,
    money double precision NOT NULL,
    overall_transactions_profit double precision NOT NULL,
    password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    user_name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (user_id),
    CONSTRAINT uk_lqjrcobrh9jc8wpcar64q1bfh UNIQUE (user_name)
);

CREATE TABLE IF NOT EXISTS public.user_crypto
(
    crypto_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    amount double precision NOT NULL,
    average_crypto_buying_price double precision NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT user_crypto_pkey PRIMARY KEY (crypto_name, user_id)
);

ALTER TABLE IF EXISTS public.transaction
    ADD CONSTRAINT fk9qvcqovc8mo7kdapyl8rbc1jt FOREIGN KEY (crypto_id)
    REFERENCES public.crypto (crypto_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.transaction
    ADD CONSTRAINT fkbqwndeew6s3ip7ajiqpne7ifr FOREIGN KEY (user_id)
    REFERENCES public."user" (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.user_crypto
    ADD CONSTRAINT fks3y3pfokvsqst2298wqdsoedo FOREIGN KEY (user_id)
    REFERENCES public."user" (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

END;