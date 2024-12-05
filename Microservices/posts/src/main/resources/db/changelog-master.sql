--liquibase formatted sql

-- changeset Sean:0
CREATE TABLE IF NOT EXISTS posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    likesCount INT,
    content VARCHAR(1024),
    userID VARCHAR(255) NOT NULL
);

-- changeset Sean:1
CREATE TABLE IF NOT EXISTS likes (
    likeId BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isLiked BOOLEAN DEFAULT TRUE NOT NULL
);


