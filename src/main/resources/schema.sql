CREATE TABLE IF NOT EXISTS venue_levels (
    level_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    level_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    seats_per_row INTEGER NOT NULL,
    rows INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS seat (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    row_number INTEGER NOT NULL,
    number INTEGER NOT NULL,
    status TINYINT NOT NULL CHECK (status BETWEEN 0 AND 2), -- Assuming SeatStatus is an enum with 0, 1, 2
    level_id INTEGER,
    FOREIGN KEY (level_id) REFERENCES venue_levels(level_id) ON DELETE CASCADE
);