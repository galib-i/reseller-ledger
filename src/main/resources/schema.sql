CREATE TABLE users (
    id            SERIAL    PRIMARY KEY,
    email         TEXT      NOT NULL UNIQUE,
    password_hash TEXT      NOT NULL
);

CREATE TABLE platforms (
    id            SERIAL    PRIMARY KEY,
    name          TEXT      NOT NULL UNIQUE
);

CREATE TABLE items (
    id            SERIAL    PRIMARY KEY,
    name          TEXT      NOT NULL,
    description   TEXT,
    brand         TEXT,
    size          TEXT,
    condition     TEXT,
    created_at    TIMESTAMP WITH TIME ZONE  DEFAULT NOW(),
    deleted_at    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE listings (
    id            SERIAL    PRIMARY KEY,
    item_id       INT       NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    platform_id   INT       NOT NULL REFERENCES platforms(id) ON DELETE CASCADE,
    listing_url   TEXT,
    listed_at     TIMESTAMP WITH TIME ZONE  DEFAULT NOW(),
    UNIQUE (item_id, platform_id)
);

CREATE TABLE transactions (
    id               SERIAL         PRIMARY KEY,
    item_id          INT            NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    user_id          INT            NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    platform_id      INT            REFERENCES platforms(id) ON DELETE CASCADE,
    type             TEXT           NOT NULL,
    value            DECIMAL(10, 2) NOT NULL,
    platform_fee     DECIMAL(10, 2),
    delivery_fee     DECIMAL(10, 2),
    transaction_date DATE           NOT NULL
);