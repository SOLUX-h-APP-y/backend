package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    List<PostImage> findAllByPostOrderByOrderSequenceAsc(Post post);
    Optional<PostImage> findTopByPostOrderByOrderSequenceAsc(Post post); // order_sequence가 가장 작은 이미지 조회
}