package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.ReviewRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, PostRepository postRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Review createReview(Long postId, Long reviewerId, Long revieweeId, Integer rate, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User reviewer = userRepository.findById(reviewerId).orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User reviewee = userRepository.findById(revieweeId).orElseThrow(() -> new RuntimeException("Reviewee not found"));

        Review review = new Review();
        review.setPost(post);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRate(rate);
        review.setContent(content);
        review.setCreateAt(LocalDateTime.now());
        review.setUpdateAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

}
