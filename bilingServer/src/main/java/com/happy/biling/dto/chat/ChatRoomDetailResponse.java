package com.happy.biling.dto.chat;

import com.happy.biling.domain.entity.enums.PostStatus;
import java.util.List;

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

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public String getOtherUserProfileImage() {
        return otherUserProfileImage;
    }

    public void setOtherUserProfileImage(String otherUserProfileImage) {
        this.otherUserProfileImage = otherUserProfileImage;
    }

    public List<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }
}
