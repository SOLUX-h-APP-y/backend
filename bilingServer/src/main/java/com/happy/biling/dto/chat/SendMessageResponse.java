package com.happy.biling.dto.chat;

public class SendMessageResponse {
    private Long chatRoomId;
    private String message;

    public SendMessageResponse(Long chatRoomId, String message) {
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
