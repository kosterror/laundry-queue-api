CREATE TABLE refresh_token
(
    id         UUID PRIMARY KEY,
    owner_id   UUID         NOT NULL REFERENCES person (id) ON DELETE CASCADE,
    expired_at TIMESTAMP    NOT NULL,
    token      varchar(255) NOT NULL
);