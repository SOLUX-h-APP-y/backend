package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.ChatMessage;
import com.happy.biling.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    long countByChatRoomAndIsReadFalse(ChatRoom chatRoom);

    Optional<ChatMessage> findTopByChatRoomOrderByCreateAtDesc(ChatRoom chatRoom);
}
