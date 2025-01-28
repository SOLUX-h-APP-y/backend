package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPostId(Long postId);
    List<Review> findByReviewerId(Long reviewerId);
    List<Review> findByRevieweeId(Long revieweeId);
    boolean existsByPostIdAndReviewerId(Long postId, Long reviewerId);
    Optional<Review> findByPostIdAndRevieweeId(Long postId, Long revieweeId);
}
