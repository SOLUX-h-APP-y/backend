package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.entity.enums.Category;
import com.happy.biling.domain.entity.enums.Distance;
import com.happy.biling.domain.entity.enums.PostType;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.post.PostWriteRequestDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 생성 (Todo 이미지 있도록 보완 필요)
    @Transactional
    public Post createPost(PostWriteRequestDto requestDto) {
        // User 객체 찾기
        User writer = userRepository.findById(requestDto.getWriterId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Post post = new Post();

        post.setWriter(writer);
        post.setType(PostType.valueOf(requestDto.getType()));
        post.setTitle(requestDto.getTitle());
        post.setPrice(requestDto.getPrice());
        post.setContent(requestDto.getContent());
        post.setDistance(Distance.valueOf(requestDto.getDistance()));
        post.setCategory(Category.valueOf(requestDto.getCategory()));
        post.setExpirationDate(LocalDateTime.now().plusDays(requestDto.getPeriod()));
        post.setLocationName(requestDto.getLocationName());
        post.setLocationLatitude(requestDto.getLocationLatitude());
        post.setLocationLongitude(requestDto.getLocationLongitude());

        postRepository.save(post);
        
        return post;
    }
}
