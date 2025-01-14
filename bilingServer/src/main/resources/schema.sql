
CREATE TABLE `users` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `kakao_id` VARCHAR(10) NOT NULL,
   `nickname` VARCHAR(10) NOT NULL,
   `profile_image` TEXT NULL,
   `location_name` TEXT NULL,
   `location_latitude` DOUBLE NULL,
   `location_longitude` DOUBLE NULL,
   `bio` TEXT NULL,
   `rental_count` INT NOT NULL default 0,
   `tier` ENUM('씨앗', '새싹', '풀', '나무', '숲', '지구') NOT NULL DEFAULT '씨앗',
   `allow_notification` BOOLEAN NOT NULL DEFAULT TRUE,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);

CREATE TABLE `posts` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `writer_id` INT NOT NULL,
   `type` ENUM('share', 'borrow') NOT NULL,
   `title` VARCHAR(20) NOT NULL,
   `content` TEXT NOT NULL,
   `price` INT NOT NULL,
   `distance` ENUM('거리무관', '3km', '5km', '10km') NOT NULL,
   `category` ENUM('헬스', '패션', '엔터', '학업', '기타') NOT NULL,
   `expiration_date` DATETIME NOT NULL,
   `location_name` TEXT NOT NULL,
   `location_latitude` DOUBLE NOT NULL,
   `location_longitude` DOUBLE NOT NULL,
   `status` ENUM('거래중', '사용중', '거래완료') NOT NULL DEFAULT '거래중',
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);

CREATE TABLE `reviews` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `post_id` INT NOT NULL,
   `reviewer_id` INT NOT NULL,
   `reviewee_id` INT NOT NULL,
   `rate` INT NOT NULL,
   `content` TEXT NOT NULL,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);

CREATE TABLE `post_images` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `post_id` INT NOT NULL,
   `image_url` TEXT NOT NULL,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `order_sequence` INT NOT NULL default 1,
   `image_type` VARCHAR(20) NULL,
   PRIMARY KEY (`id`)
);

CREATE TABLE `chat_messages` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `chatroom_id` INT NOT NULL,
   `sender_id` INT NOT NULL,
   `content` TEXT NOT NULL,
   `is_read` BOOLEAN NOT NULL DEFAULT false,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);

CREATE TABLE `user_message_read_status` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `user_id` INT NOT NULL,
   `chatroom_id` INT NOT NULL,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `unread_chat_count` INT NOT NULL default 0,
   `chatroom_entry_time` DATETIME NOT NULL DEFAULT current_timestamp,
   PRIMARY KEY (`id`)
);

CREATE TABLE `chat_rooms` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `post_id` INT NOT NULL,
   `renter_id` INT NOT NULL,
   `owner_id` INT NOT NULL,
   `last_message_content` TEXT NULL,
   `last_message_time` DATETIME NULL,
   `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   `update_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);

-- user 테이블의 id를 참조하는 외래키
ALTER TABLE `posts`
    ADD CONSTRAINT `FK_POST_USER`
    FOREIGN KEY (`writer_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- post 테이블의 id를 참조하는 외래키
ALTER TABLE `reviews`
    ADD CONSTRAINT `FK_REVIEW_POST`
    FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- user 테이블의 id를 참조하는 외래키
ALTER TABLE `reviews`
    ADD CONSTRAINT `FK_REVIEW_REVIEWER`
    FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- user 테이블의 id를 참조하는 외래키
ALTER TABLE `reviews`
    ADD CONSTRAINT `FK_REVIEW_REVIEWEE`
    FOREIGN KEY (`reviewee_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- post 테이블의 id를 참조하는 외래키
ALTER TABLE `post_images`
    ADD CONSTRAINT `FK_POST_IMAGE_POST`
    FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- chat_room 테이블의 id를 참조하는 외래키
ALTER TABLE `chat_messages`
    ADD CONSTRAINT `FK_CHAT_MESSAGE_CHAT_ROOM`
    FOREIGN KEY (`chatroom_id`) REFERENCES `chat_rooms` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- user 테이블의 id를 참조하는 외래키
ALTER TABLE `user_message_read_status`
    ADD CONSTRAINT `FK_USER_MESSAGE_READ_STATUS_USER`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- chat_room 테이블의 id를 참조하는 외래키
ALTER TABLE `user_message_read_status`
    ADD CONSTRAINT `FK_USER_MESSAGE_READ_STATUS_CHAT_ROOM`
    FOREIGN KEY (`chatroom_id`) REFERENCES `chat_rooms` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- post 테이블의 id를 참조하는 외래키
ALTER TABLE `chat_rooms`
    ADD CONSTRAINT `FK_CHAT_ROOM_POST`
    FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- user 테이블의 id를 참조하는 외래키 (renter_id, owner_id)
ALTER TABLE `chat_rooms`
    ADD CONSTRAINT `FK_CHAT_ROOM_RENTER`
    FOREIGN KEY (`renter_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE `chat_rooms`
    ADD CONSTRAINT `FK_CHAT_ROOM_OWNER`
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- user_message_read_status 테이블에 user_id, chatroom_id 컬럼에 복합 인덱스 추가
CREATE INDEX idx_user_message_user_chatroom ON user_message_read_status (user_id, chatroom_id);

-- posts 테이블에 writer_id, status 컬럼에 인덱스 추가
CREATE INDEX idx_post_writer_id ON posts (writer_id);
CREATE INDEX idx_post_status ON posts (status);

-- chat_rooms 테이블에 post_id, renter_id, owner_id 컬럼에 인덱스 추가
CREATE INDEX idx_chat_room_post_id ON chat_rooms (post_id);
CREATE INDEX idx_chat_room_renter_id ON chat_rooms (renter_id);
CREATE INDEX idx_chat_room_owner_id ON chat_rooms (owner_id);
