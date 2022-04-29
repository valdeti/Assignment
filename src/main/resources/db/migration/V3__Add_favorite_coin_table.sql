CREATE TABLE favorite_coins
(
    user_id VARCHAR(36) NOT NULL,
    coin_id VARCHAR(36) NOT NULL,
    CONSTRAINT users_roles_pkey
        PRIMARY KEY (user_id, coin_id),
    CONSTRAINT coin_id_fk
        FOREIGN KEY (coin_id) REFERENCES coins,
    CONSTRAINT user_id_fk
        FOREIGN KEY (user_id) REFERENCES users
);
