package com.happy.biling.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long postId;
    private Long reviewerId;
    private Long revieweeId;
    private Integer rate;
    private String content;
}
