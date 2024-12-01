--liquibase formatted sql

        -- changeset Sean:0
        CREATE TABLE posts (
        id INT AUTO_INCREMENT PRIMARY KEY,
        likesCount INT,
        content VARCHAR(1024),
        userID VARCHAR(255) NOT NULL
        );

        -- changeset Sean:1
        CREATE TABLE likes (
        likeId BIGINT AUTO_INCREMENT PRIMARY KEY,
        userId BIGINT NOT NULL,
        postId BIGINT NOT NULL,
        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        isLiked BOOLEAN DEFAULT TRUE NOT NULL
        );

        ALTER TABLE likes
        ADD CONSTRAINT FK_LIKES_USERID FOREIGN KEY (userId) REFERENCES users (id);

        ALTER TABLE likes
        ADD CONSTRAINT FK_LIKES_POSTID FOREIGN KEY (postId) REFERENCES posts (id);
