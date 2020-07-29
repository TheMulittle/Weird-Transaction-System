DROP TABLE IF EXISTS configuration;

CREATE TABLE configuration
(
    name VARCHAR(250) PRIMARY KEY,
    value VARCHAR(250) NOT NULL
);

INSERT INTO configuration
    (name, value)
VALUES
    ('MAX_AMOUNT', '50000');