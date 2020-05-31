CREATE TABLE tour_package (
    code char(5) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE tour (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_package_code CHAR(5) NOT NULL,
    tour_title VARCHAR(100) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    blurb VARCHAR(2000) NOT NULL,
    bullets VARCHAR(2000) NOT NULL,
    price VARCHAR(100) NOT NULL,
    duration VARCHAR(32) NOT NULL,
    difficulty VARCHAR(16) NOT NULL,
    region VARCHAR(20) NOT NULL,
    keywords VARCHAR(100)
);

ALTER TABLE tour ADD FOREIGN KEY (tour_package_code) REFERENCES tour_package(code);

CREATE TABLE tour_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT,
    customer_id BIGINT,
    rating_score INT,
    comment VARCHAR(100)
);

ALTER TABLE tour_rating ADD UNIQUE TourCustomerConstraint (tour_id, customer_id);