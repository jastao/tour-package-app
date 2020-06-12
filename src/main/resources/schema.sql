CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    first_name VARCHAR(256) NOT NULL,
    last_name VARCHAR(256) NOT NULL,
    email VARCHAR(50) NOT NULL
);

CREATE TABLE auth_user_group (
    auth_user_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_group VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL
);

CREATE TABLE user_auth_user_group (
    username VARCHAR(128) NOT NULL,
    auth_user_group_id BIGINT NOT NULL,
    CONSTRAINT username_fk FOREIGN KEY (username) REFERENCES user (username),
    CONSTRAINT auth_user_group_id_fk FOREIGN KEY (auth_user_group_id) REFERENCES auth_user_group (auth_user_group_id),
    UNIQUE(username, auth_user_group_id)
);

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

ALTER TABLE tour ADD FOREIGN KEY (tour_package_code) REFERENCES tour_package (code);

CREATE TABLE tour_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT,
    customer_id BIGINT,
    rating_score INT,
    comment VARCHAR(100)
);

ALTER TABLE tour_rating ADD UNIQUE TourCustomerConstraint (tour_id, customer_id);