package com.happy.biling.controller;

import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.service.ReviewService;
import com.happy.biling.dto.review.ReviewRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        Long postId = reviewRequestDto.getPostId();
        Long reviewerId = reviewRequestDto.getReviewerId();
        Long revieweeId = reviewRequestDto.getRevieweeId();
        Integer rate = reviewRequestDto.getRate();
        String content = reviewRequestDto.getContent();

        Review review = reviewService.createReview(postId, reviewerId, revieweeId, rate, content);
        return ResponseEntity.ok(review);
    }
}
