package com.happy.biling.controller;

import com.happy.biling.domain.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/unread")
    public ResponseEntity<Integer> getTotalUnreadChatCount(@RequestParam Long userId) {
        Integer unreadCount = chatService.getTotalUnreadChatCount(userId);
        return ResponseEntity.ok(unreadCount);
    }
}
