--liquibase formatted sql

-- changeset Milan:0
CREATE TABLE IF NOT EXISTS tokens (
  user_id BIGINT NOT NULL,
  refresh_token VARCHAR(500) NOT NULL,
    expiration_time_access_token DATETIME,
    expiration_time_refresh_token DATETIME,
    current_access_token VARCHAR(500) NOT NULL,
    PRIMARY KEY (user_id)
    );

-- changeset Milan:1
CREATE TABLE IF NOT EXISTS users (
 user_id BIGINT AUTO_INCREMENT NOT NULL,
 username VARCHAR(200),
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200),
    full_name VARCHAR(200),
    bio TEXT,
    profile_pic VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    follower_count INT,
    following_count INT,
    PRIMARY KEY (user_id)
    );

-- changeset Milan:2
CREATE TABLE IF NOT EXISTS jwt_refresh_tokens(
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    refresh_token VARCHAR(200) NOT NULL,
    expiry_date DATETIME,
    PRIMARY KEY (id)
    );
