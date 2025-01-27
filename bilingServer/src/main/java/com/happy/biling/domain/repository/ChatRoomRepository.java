package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.ChatRoom;
import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByPostAndOwnerAndRenter(Post post, User owner, User renter);
}
