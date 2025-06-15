--DROP TABLE IF EXISTS users;
--
--CREATE TABLE users (
--    id BIGINT PRIMARY KEY,
--    name VARCHAR(255),
--    email VARCHAR(255),
--    age INT,
--    created_at VARCHAR(32)
--);

INSERT INTO users (id, name, email, age, created_at)
VALUES (1, 'Test User', 'test@mail.ru', 20, '2025-06-08T15:32:08');