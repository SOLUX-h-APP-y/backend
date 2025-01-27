package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.ChatMessage;
import com.happy.biling.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByCreateAtAsc(ChatRoom chatRoom);
    Optional<ChatMessage> findTopByChatRoomOrderByCreateAtDesc(ChatRoom chatRoom);
}
