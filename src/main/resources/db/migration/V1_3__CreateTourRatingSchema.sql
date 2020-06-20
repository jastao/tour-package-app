CREATE TABLE tour_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT,
    customer_id BIGINT,
    rating_score INT,
    comment VARCHAR(100)
);
ALTER TABLE tour_rating ADD CONSTRAINT FK_TOUR_ID FOREIGN KEY (tour_id) REFERENCES tour (id);
ALTER TABLE tour_rating ADD CONSTRAINT UN_TOUR_CUSTOMER UNIQUE (tour_id, customer_id);