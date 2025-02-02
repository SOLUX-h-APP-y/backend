package com.happy.biling.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageRequest {
    private Long postId;
    private Long ownerId;
    private Long renterId;
    private Long chatRoomId;
    private String content;

}
