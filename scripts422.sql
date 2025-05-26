CREATE TABLE car(
    id BIGINT PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    price NUMERIC (10, 2) NOT NULL,
);

CREATE TABLE driver(
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age SMALLINT NOT NULL,
    license BOOLEAN NOT NULL,
    car_id  BIGINT,
    FOREIGN KEY (car_id) REFERENCES car(id)
);