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
    auth_user_group_id BIGINT NOT NULL
);

ALTER TABLE user_auth_user_group ADD CONSTRAINT FK_USERNAME FOREIGN KEY (username) REFERENCES user (username);
ALTER TABLE user_auth_user_group ADD CONSTRAINT FK_AUTH_USER_GROUP_ID FOREIGN KEY (auth_user_group_id) REFERENCES auth_user_group (auth_user_group_id);
ALTER TABLE user_auth_user_group ADD CONSTRAINT UN_AUTH_USER_GROUP UNIQUE (username, auth_user_group_id);
