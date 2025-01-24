package com.happy.biling.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDetailResponseDto {
	private Boolean isMyPost;
	private Long writerId;
	private String writerProfileImage;
	private String writerNickname;
    private String distance;
    private String category;
    private String title;
    private LocalDateTime createAt;
    private String content;
    private Integer price;
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
    private List<String> imageUrls;
}
