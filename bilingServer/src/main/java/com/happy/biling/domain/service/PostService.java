package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.PostImage;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.entity.enums.Category;
import com.happy.biling.domain.entity.enums.Distance;
import com.happy.biling.domain.entity.enums.PostType;
import com.happy.biling.domain.repository.PostImageRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.post.PostDetailResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;

    // 게시글 생성 (TODO 이미지 있도록 보완 필요)
    @Transactional
    public Post createPost(PostWriteRequestDto requestDto, Long userId) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Post post = new Post();

        post.setWriter(writer);
        post.setType(PostType.valueOf(requestDto.getType()));
        post.setTitle(requestDto.getTitle());
        post.setPrice(requestDto.getPrice());
        post.setContent(requestDto.getContent());
        post.setDistance(Distance.valueOf(requestDto.getDistance()));
        post.setCategory(Category.valueOf(requestDto.getCategory()));
        post.setExpirationDate(LocalDateTime.now().plusDays(180));
        post.setLocationName(requestDto.getLocationName());
        post.setLocationLatitude(requestDto.getLocationLatitude());
        post.setLocationLongitude(requestDto.getLocationLongitude());

        postRepository.save(post);
        
        return post;
    }
    
    public PostDetailResponseDto getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<String> imageUrls = postImageRepository.findAllByPostOrderByOrderSequenceAsc(post)
                .stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());

        PostDetailResponseDto responseDto = new PostDetailResponseDto();
        responseDto.setIsMyPost(post.getWriter().getId().equals(userId));
        responseDto.setWriterId(post.getWriter().getId());
        String profileImage = Optional.ofNullable(post.getWriter().getProfileImage())
                .orElse("");
        responseDto.setWriterProfileImage(profileImage);
        responseDto.setWriterNickname(post.getWriter().getNickname());
        responseDto.setDistance(post.getDistance().name());
        responseDto.setCategory(post.getCategory().name());
        responseDto.setTitle(post.getTitle());
        responseDto.setCreateAt(post.getCreateAt());
        responseDto.setContent(post.getContent());
        responseDto.setPrice(post.getPrice());
        responseDto.setLocationName(post.getLocationName());
        responseDto.setLocationLatitude(post.getLocationLatitude());
        responseDto.setLocationLongitude(post.getLocationLongitude());
        responseDto.setImageUrls(imageUrls);

        return responseDto;
    }
}
