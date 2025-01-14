package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
