package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    List<PostImage> findAllByPostOrderByOrderSequenceAsc(Post post);
}