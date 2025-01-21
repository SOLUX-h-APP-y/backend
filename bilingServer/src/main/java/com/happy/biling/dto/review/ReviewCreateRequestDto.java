package com.happy.biling.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private Long revieweeId; // 리뷰 대상자 ID
    private Integer rate;    // 평점
    private String content;  // 평가 내용
}