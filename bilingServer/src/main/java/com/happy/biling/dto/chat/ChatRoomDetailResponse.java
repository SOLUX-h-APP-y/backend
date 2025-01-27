package com.happy.biling.dto.chat;

import java.util.List;

public class ChatRoomDetailResponse {
    private String postTitle;
    private String postLocation;
    private String postImage;
    private String otherUserProfileImage;
    private List<MessageResponse> messages;

    public ChatRoomDetailResponse(String postTitle, String postLocation, String postImage, String otherUserProfileImage, List<MessageResponse> messages) {
        this.postTitle = postTitle;
        this.postLocation = postLocation;
        this.postImage = postImage;
        this.otherUserProfileImage = otherUserProfileImage;
        this.messages = messages;
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
