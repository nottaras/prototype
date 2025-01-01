ALTER TABLE entries
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP;

UPDATE entries
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

ALTER TABLE entries
    ALTER COLUMN created_at SET NOT NULL;
