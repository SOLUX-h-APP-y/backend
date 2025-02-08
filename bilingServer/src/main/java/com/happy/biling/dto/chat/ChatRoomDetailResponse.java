package com.happy.biling.dto.chat;

import com.happy.biling.domain.entity.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChatRoomDetailResponse {
    private Long chatRoomId;
    private Long writerId;
    private String postTitle;
    private String postLocation;
    private String postImage;
    private PostStatus postStatus;
    private String otherUserProfileImage;
    private List<MessageResponse> messages;

    public ChatRoomDetailResponse(Long chatRoomId, Long writerId, String postTitle, String postLocation, String postImage, PostStatus postStatus, String otherUserProfileImage, List<MessageResponse> messages) {
        this.chatRoomId = chatRoomId;
        this.writerId = writerId;
        this.postTitle = postTitle;
        this.postLocation = postLocation;
        this.postImage = postImage;
        this.postStatus = postStatus;
        this.otherUserProfileImage = otherUserProfileImage;
        this.messages = messages;
    }

}
