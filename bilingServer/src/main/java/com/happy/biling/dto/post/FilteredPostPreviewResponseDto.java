package com.happy.biling.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FilteredPostPreviewResponseDto {
    private Long postId;          // 게시글 ID
    private String title;         // 게시글 제목
    private Integer price;        // 가격
    private String previewImage;  // 대표 이미지 URL
    private String locationName;  // 위치 이름
}


