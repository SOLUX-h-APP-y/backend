package com.happy.biling.dto.chat;

import java.time.LocalDateTime;

public class MessageResponse {
    private String sender;
    private String content;
    private LocalDateTime createAt;

    public MessageResponse(String sender, String content, LocalDateTime createAt) {
        this.sender = sender;
        this.content = content;
        this.createAt = createAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
