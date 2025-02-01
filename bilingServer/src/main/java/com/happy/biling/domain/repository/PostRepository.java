package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;

import com.happy.biling.domain.entity.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findById(Long id);
    List<Post> findByWriterId(Long writerId);
    List<Post> findByType(String type);
    List<Post> findByStatus(String status);
    List<Post> findAllByOrderByCreateAtDesc();
    long countByWriterIdAndStatus(Long writerId, PostStatus status);
}