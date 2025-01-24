package com.happy.biling.controller;

import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.service.ReviewService;
import com.happy.biling.dto.review.ReviewCreateRequestDto;
import com.happy.biling.dto.review.ReviewResponseDto;
import com.happy.biling.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // 특정 게시물의 리뷰 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<ReviewResponseDto>> getPostReviews(
            @PathVariable Long postId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByPostId(postId);
        return ResponseEntity.ok(reviews);
    }

    // 내가 작성한 리뷰 조회
    @GetMapping("/me/written")
    public ResponseEntity<List<ReviewResponseDto>> getMyWrittenReviews() {
        Long userId = getCurrentUserId();
        List<ReviewResponseDto> reviews = reviewService.getReviewsByReviewerId(userId);
        return ResponseEntity.ok(reviews);
    }

    // 내가 받은 리뷰 조회
    @GetMapping("/me/received")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews() {
        Long userId = getCurrentUserId();
        List<ReviewResponseDto> reviews = reviewService.getReviewsByRevieweeId(userId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 작성
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST})
    @PostMapping("/{postId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long postId,
            @RequestBody ReviewCreateRequestDto requestDto) {
        log.info("리뷰 작성 요청: postId={}, requestDto={}", postId, requestDto);
        Long reviewerId = getCurrentUserId();
        ReviewResponseDto review = reviewService.createReview(postId, requestDto, reviewerId);
        return ResponseEntity.ok(review);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            String userIdStr = (String) authentication.getPrincipal();
            return Long.parseLong(userIdStr);
        }
        throw new RuntimeException("인증되지 않은 사용자입니다.");
    }
}
