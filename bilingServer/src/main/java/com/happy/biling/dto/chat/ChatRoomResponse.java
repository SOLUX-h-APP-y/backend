package com.happy.biling.dto.chat;

import java.time.LocalDateTime;

public class ChatRoomResponse {
    private String profileImage;
    private String postTitle;
    private String lastMessageContent;
    private long unreadCount;
    private LocalDateTime lastMessageTimestamp;

    public ChatRoomResponse(String profileImage, String postTitle, String lastMessageContent, long unreadCount, LocalDateTime lastMessageTimestamp) {
        this.profileImage = profileImage;
        this.postTitle = postTitle;
        this.lastMessageContent = lastMessageContent;
        this.unreadCount = unreadCount;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public LocalDateTime getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(LocalDateTime lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
