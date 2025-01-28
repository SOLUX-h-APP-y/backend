package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.ChatMessage;
import com.happy.biling.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByCreateAtAsc(ChatRoom chatRoom);
    Optional<ChatMessage> findTopByChatRoomOrderByCreateAtDesc(ChatRoom chatRoom);
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId ORDER BY cm.createAt DESC")
    ChatMessage findFirstByChatRoomIdOrderByCreateAtDesc(@Param("chatRoomId") Long chatRoomId);
}
