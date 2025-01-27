package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.ChatRoom;
import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByOwnerOrRenter(User owner, User renter);
    ChatRoom findByPostAndOwnerAndRenter(Post post, User owner, User renter);
}
