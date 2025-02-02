package com.happy.biling.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageResponse {
    private Long chatRoomId;
    private String message;

    public SendMessageResponse(Long chatRoomId, String message) {
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

}
