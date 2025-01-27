package com.happy.biling.controller;

import com.happy.biling.dto.chat.ChatRoomDetailResponse;
import com.happy.biling.dto.chat.ChatRoomResponse;
import com.happy.biling.domain.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRoomController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/chat/{userId}/rooms")
    public List<ChatRoomResponse> getChatRooms(@PathVariable Long userId) {
        return chatMessageService.getChatRooms(userId);
    }

    @GetMapping("/chat/rooms/{chatRoomId}/details")
    public ChatRoomDetailResponse getChatRoomDetails(@PathVariable Long chatRoomId) {
        return chatMessageService.getChatRoomDetails(chatRoomId);
    }
}
