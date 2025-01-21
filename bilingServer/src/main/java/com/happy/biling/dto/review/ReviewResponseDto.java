package com.happy.biling.dto.review;

import com.happy.biling.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Integer rate;      // 평점
    private String content;    // 평가 내용

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .rate(review.getRate())
                .content(review.getContent())
                .build();
    }
}
