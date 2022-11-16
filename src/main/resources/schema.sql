CREATE TABLE public.users
(
    id         bigserial NOT NULL PRIMARY KEY,
    username   text      NOT NULL UNIQUE,
    password   text      NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE public.feature
(
    id                  bigserial NOT NULL PRIMARY KEY,
    name                text      NOT NULL UNIQUE,
    description         text,
    is_enabled_globally boolean   NOT NULL DEFAULT false,
    created_at          timestamp NOT NULL DEFAULT now(),
    updated_at          timestamp
);

CREATE TABLE public.role
(
    id         bigserial NOT NULL PRIMARY KEY,
    name       text      NOT NULL UNIQUE ,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE public.user_feature
(
    user_id    bigserial NOT NULL,
    feature_id bigserial NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, feature_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_feature
        FOREIGN KEY (feature_id)
            REFERENCES feature (id)
);

CREATE TABLE public.user_role
(
    user_id    bigserial NOT NULL,
    role_id    bigserial NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_role
        FOREIGN KEY (role_id)
            REFERENCES role (id)
);

