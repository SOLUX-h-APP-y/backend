package com.happy.biling.controller;

import com.happy.biling.dto.chat.ChatRoomDetailResponse;
import com.happy.biling.dto.chat.ChatRoomResponse;
import com.happy.biling.dto.chat.ChatroomPostResponseDto;
import com.happy.biling.dto.post.FilteredPostPreviewResponseDto;
import com.happy.biling.dto.post.PostPreviewResponseDto;
import com.happy.biling.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.happy.biling.domain.service.ChatMessageService;
import com.happy.biling.domain.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {
	
	private final JwtUtil jwtUtil;
	private final PostService postService;

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
    
    //채팅방 상단 게시글 정보 반환
    @GetMapping("/chat/rooms/post/{id}")
    public ResponseEntity<ChatroomPostResponseDto> getChatRoomPostSummary(
            @PathVariable("id") Long postId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            Long userId;

            String token = authHeader.substring(7); // "Bearer " 제거
            userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));


            ChatroomPostResponseDto post = postService.getChatroomPost(postId, userId);
            return ResponseEntity.ok(post);

        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
