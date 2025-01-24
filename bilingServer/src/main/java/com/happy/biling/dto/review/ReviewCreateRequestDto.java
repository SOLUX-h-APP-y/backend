package com.happy.biling.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewCreateRequestDto {
    private Long revieweeId; // 리뷰 대상자 ID
    private Integer rate;    // 평점
    private String content;  // 평가 내용
}