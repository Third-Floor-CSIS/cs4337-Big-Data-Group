--liquibase formatted sql

-- changeset Brendan:0
CREATE TABLE profile (
    user_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    bio VARCHAR(255),
    profile_pic VARCHAR(255),
    count_follower INT,
    count_following INT,
    PRIMARY KEY (user_id)
);

-- changeset Milan:1
CREATE TABLE following (
    initiator_id VARCHAR(255) NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('active', 'unfollowed', 'pending'),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (initiator_id, target_id)
);


