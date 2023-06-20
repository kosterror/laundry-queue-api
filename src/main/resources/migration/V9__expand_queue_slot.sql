ALTER TABLE queue_slot
    ADD COLUMN status_changed TIMESTAMP,
    ADD COLUMN status        VARCHAR(128);