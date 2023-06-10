CREATE TABLE dormitory
(
    id      UUID PRIMARY KEY,
    name    VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE person
(
    id             UUID PRIMARY KEY,
    dormitory_id   UUID        REFERENCES dormitory (id) ON DELETE SET NULL,
    student_number VARCHAR(6),
    name           VARCHAR(64) NOT NULL,
    surname        VARCHAR(64) NOT NULL,
    room           VARCHAR(16),
    money_count    NUMERIC     NOT NULL,
    role           VARCHAR(64) NOT NULL
);

CREATE TABLE machine
(
    id           UUID PRIMARY KEY,
    dormitory_id UUID REFERENCES dormitory (id) ON DELETE CASCADE,
    owner_id     UUID REFERENCES person (id) ON DELETE CASCADE,
    type         VARCHAR(64) NOT NULL,
    status       VARCHAR(64) NOT NULL,
    start_time   TIMESTAMP
);

CREATE TABLE queue_slot
(
    id         UUID PRIMARY KEY,
    machine_id UUID REFERENCES machine (id) ON DELETE CASCADE NOT NULL,
    person_id  UUID                                           REFERENCES person (id) ON DELETE SET NULL,
    number     INTEGER                                        NOT NULL,
    UNIQUE (machine_id, number)
);

