ALTER TABLE entries
    ADD COLUMN notes TEXT;

UPDATE entries
SET notes = '';

ALTER TABLE entries
    ALTER COLUMN notes SET NOT NULL;