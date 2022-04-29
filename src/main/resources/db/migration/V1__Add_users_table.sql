CREATE TABLE users
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL
);
