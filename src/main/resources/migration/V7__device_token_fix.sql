ALTER TABLE person
    DROP COLUMN device_token;

CREATE TABLE device_token
(
    id           UUID PRIMARY KEY,
    created_date TIMESTAMP     NOT NULL,
    token        VARCHAR(1024) NOT NULL,
    owner_id     UUID REFERENCES person (id) ON DELETE CASCADE
);