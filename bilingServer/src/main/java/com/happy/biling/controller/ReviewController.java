package com.happy.biling.controller;

import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.service.ReviewService;
import com.happy.biling.dto.review.ReviewCreateRequestDto;
import com.happy.biling.dto.review.ReviewResponseDto;
import com.happy.biling.security.JwtAuthentication;
import com.happy.biling.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final JwtUtil jwtUtil;

    @GetMapping("/{postId}")
    public ResponseEntity<List<ReviewResponseDto>> getPostReviews(
            @PathVariable Long postId) {
        try {
            List<ReviewResponseDto> reviews = reviewService.getReviewsByPostId(postId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error getting post reviews: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me/written")
    public ResponseEntity<List<ReviewResponseDto>> getMyWrittenReviews(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("Getting written reviews for userId: {}", userId);

            List<ReviewResponseDto> reviews = reviewService.getReviewsByReviewerId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error getting written reviews: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me/received")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("Getting received reviews for userId: {}", userId);

            List<ReviewResponseDto> reviews = reviewService.getReviewsByRevieweeId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error getting received reviews: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.POST})
    @PostMapping("/{postId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long postId,
            @RequestBody ReviewCreateRequestDto requestDto) {
        try {
            String token = authHeader.substring(7);
            Long reviewerId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("Creating review: postId={}, reviewerId={}", postId, reviewerId);

            ReviewResponseDto review = reviewService.createReview(postId, requestDto, reviewerId);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            log.error("Error creating review: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
