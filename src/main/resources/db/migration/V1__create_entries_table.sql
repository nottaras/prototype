CREATE TABLE IF NOT EXISTS entries
(
    id   BIGSERIAL PRIMARY KEY,
    date DATE        NOT NULL,
    mood VARCHAR(20) NOT NULL,
    CONSTRAINT unique_date UNIQUE (date)
);