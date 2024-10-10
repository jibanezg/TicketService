INSERT INTO venue_levels (level_name, price, seats_per_row, rows) VALUES('Orchestra',100.00, 50, 25);
INSERT INTO venue_levels (level_name, price, seats_per_row, rows) VALUES('Main',75.00, 20, 100);
INSERT INTO venue_levels (level_name, price, seats_per_row, rows) VALUES('Balcony 1',50.00, 15, 100);
INSERT INTO venue_levels (level_name, price, seats_per_row, rows) VALUES('Balcony 2',40.00, 15, 100);

-- Orchestra Level
INSERT INTO seat (row_number, number, status, level_id) VALUES (1, 1, 0, 1);
-- ... (continue for all seats in 25 rows with 50 seats each)
INSERT INTO seat (row_number, number, status, level_id) VALUES (25, 50, 0, 1);

-- Main Level
INSERT INTO seat (row_number, number, status, level_id) VALUES (1, 1, 0, 2);
-- ... (continue for all seats in 100 rows with 20 seats each)
INSERT INTO seat (row_number, number, status, level_id) VALUES (100, 20, 0, 2);

-- Balcony 1 Level
INSERT INTO seat (row_number, number, status, level_id) VALUES (1, 1, 0, 3);
-- ... (continue for all seats in 100 rows with 15 seats each)
INSERT INTO seat (row_number, number, status, level_id) VALUES (100, 15, 0, 3);

-- Balcony 2 Level
INSERT INTO seat (row_number, number, status, level_id) VALUES (1, 1, 0, 4);
-- ... (continue for all seats in 100 rows with 15 seats each)
INSERT INTO seat (row_number, number, status, level_id) VALUES (100, 15, 0, 4);