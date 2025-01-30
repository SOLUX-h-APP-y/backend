package com.happy.biling.dto.chat;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponse {
    private Long chatRoomId;
    private Long postId;
    private String profileImage;
    private String postTitle;
    private String lastMessageContent;
    private long unreadCount;
    private LocalDateTime lastMessageTimestamp;

    public ChatRoomResponse(Long chatRoomId, Long postId, String profileImage, String postTitle, String lastMessageContent, long unreadCount, LocalDateTime lastMessageTimestamp) {
        this.chatRoomId = chatRoomId;
        this.postId = postId;
        this.profileImage = profileImage;
        this.postTitle = postTitle;
        this.lastMessageContent = lastMessageContent;
        this.unreadCount = unreadCount;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
