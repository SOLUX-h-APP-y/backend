package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.ReviewRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.review.ReviewCreateRequestDto;
import com.happy.biling.dto.review.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 특정 게시물의 리뷰 조회
    public List<ReviewResponseDto> getReviewsByPostId(Long postId) {
        List<Review> reviews = reviewRepository.findByPostId(postId);
        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    // 내가 작성한 리뷰 조회
    public List<ReviewResponseDto> getReviewsByReviewerId(Long reviewerId) {
        List<Review> reviews = reviewRepository.findByReviewerId(reviewerId);
        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    // 내가 받은 리뷰 조회
    public List<ReviewResponseDto> getReviewsByRevieweeId(Long revieweeId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(revieweeId);
        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    // 리뷰 작성
    @Transactional
    public ReviewResponseDto createReview(Long postId, ReviewCreateRequestDto requestDto, Long reviewerId) {
        // 게시물 존재 확인
        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        // 리뷰 대상자 존재 확인
        userRepository.findById(requestDto.getRevieweeId())
                .orElseThrow(() -> new RuntimeException("리뷰 대상자를 찾을 수 없습니다."));

        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByPostIdAndReviewerId(postId, reviewerId)) {
            throw new RuntimeException("이미 리뷰를 작성했습니다.");
        }

        // 자신에 대한 리뷰 작성 방지
        if (reviewerId.equals(requestDto.getRevieweeId())) {
            throw new RuntimeException("자신에 대한 리뷰는 작성할 수 없습니다.");
        }

        Review review = Review.builder()
                .postId(postId)
                .reviewerId(reviewerId)
                .revieweeId(requestDto.getRevieweeId())
                .rate(requestDto.getRate())
                .content(requestDto.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDto.from(savedReview);
    }
}
