CREATE TABLE professionals (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    specialty    VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uk_professionals_user UNIQUE (user_id),
    CONSTRAINT fk_professionals_user FOREIGN KEY (user_id) REFERENCES users (id)
);