CREATE TABLE dormitory
(
    id          UUID PRIMARY KEY,
    number      INTEGER NOT NULL UNIQUE CHECK ( number > 0 ),
    director_id UUID
);

CREATE TABLE person
(
    id             UUID PRIMARY KEY,
    dormitory_id   UUID,
    student_number VARCHAR(6) UNIQUE,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password       VARCHAR(512) NOT NULL,
    name           VARCHAR(64)  NOT NULL,
    surname        VARCHAR(64)  NOT NULL,
    room           VARCHAR(16),
    money          NUMERIC      NOT NULL,
    role           VARCHAR(64)  NOT NULL,
    status         VARCHAR(64)  NOT NULL
);

CREATE TABLE machine
(
    id           UUID PRIMARY KEY,
    dormitory_id UUID,
    type         VARCHAR(64) NOT NULL,
    status       VARCHAR(64) NOT NULL,
    start_time   TIMESTAMP
);

CREATE TABLE queue_slot
(
    id         UUID PRIMARY KEY,
    machine_id UUID    NOT NULL,
    person_id  UUID,
    number     INTEGER NOT NULL,
    UNIQUE (machine_id, number)
);

ALTER TABLE dormitory
    ADD CONSTRAINT fk_dormitory_director FOREIGN KEY (director_id) REFERENCES person (id);

ALTER TABLE person
    ADD CONSTRAINT fk_person_dormitory FOREIGN KEY (dormitory_id) REFERENCES dormitory (id) ON DELETE SET NULL;

ALTER TABLE machine
    ADD CONSTRAINT fk_machine_dormitory FOREIGN KEY (dormitory_id) REFERENCES dormitory (id) ON DELETE CASCADE;

ALTER TABLE queue_slot
    ADD CONSTRAINT fk_queue_slot_machine FOREIGN KEY (machine_id) REFERENCES machine (id) ON DELETE CASCADE;

ALTER TABLE queue_slot
    ADD CONSTRAINT fk_queue_slot_person FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE SET NULL;
