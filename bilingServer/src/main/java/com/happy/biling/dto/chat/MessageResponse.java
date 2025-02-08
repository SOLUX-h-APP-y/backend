package com.happy.biling.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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

}
