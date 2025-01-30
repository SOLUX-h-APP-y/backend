package com.happy.biling.controller;

import com.happy.biling.dto.chat.SendMessageRequest;
import com.happy.biling.dto.chat.SendMessageResponse;
import com.happy.biling.dto.chat.ChatRoomResponse;
import com.happy.biling.domain.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/send")
    public ResponseEntity<SendMessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        SendMessageResponse response = chatMessageService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(
            @RequestParam(value = "userId") Long userId) {
        List<ChatRoomResponse> chatRooms = chatMessageService.getChatRooms(userId);
        return ResponseEntity.ok(chatRooms);
    }

    @PutMapping("/read/{chatRoomId}")
    public ResponseEntity<String> markMessagesAsRead(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {

        chatMessageService.markMessagesAsRead(chatRoomId, userId);

        return ResponseEntity.ok("Messages marked as read");
    }
}
