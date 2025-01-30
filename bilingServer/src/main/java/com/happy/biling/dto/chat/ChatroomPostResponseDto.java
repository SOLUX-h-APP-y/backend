package com.happy.biling.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatroomPostResponseDto {
    private String postStatus;   // 거래 상태 (거래중, 대여중, 거래완료)
    private Long postId;         // 게시글 ID
    private String title;        // 게시글 제목
    private String previewImage; // 대표 이미지 URL
    private String locationName; // 위치 이름
    private Long reviewId;       // 리뷰 ID (거래완료일 경우)

    public ChatroomPostResponseDto(String postStatus, Long postId, String title, String previewImage, String locationName, Long reviewId) {
        this.postStatus = postStatus;
        this.postId = postId;
        this.title = title;
        this.previewImage = previewImage;
        this.locationName = locationName;
        this.reviewId = reviewId;
    }
}
