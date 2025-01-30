package com.happy.biling.dto.chat;

import java.time.LocalDateTime;

public class MessageResponse {
    private String sender;
    private Long senderUserId;
    private String content;
    private LocalDateTime createAt;

    public MessageResponse(String sender, Long senderUserId, String content, LocalDateTime createAt) {
        this.sender = sender;
        this.senderUserId = senderUserId;
        this.content = content;
        this.createAt = createAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
