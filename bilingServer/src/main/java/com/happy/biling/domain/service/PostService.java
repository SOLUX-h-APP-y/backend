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
import com.happy.biling.dto.post.PostPreviewResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

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
        post.setDistance(Distance.fromValue(requestDto.getDistance()));
        post.setCategory(Category.valueOf(requestDto.getCategory()));
        post.setExpirationDate(LocalDateTime.now().plusDays(180));
        post.setLocationName(requestDto.getLocationName());
        post.setLocationLatitude(requestDto.getLocationLatitude());
        post.setLocationLongitude(requestDto.getLocationLongitude());

        postRepository.save(post);

        // 이미지 업로드 처리
        if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
            List<String> imageUrls = s3Uploader.uploadMultipleFiles(requestDto.getImages(), "posts/" + post.getId());
            int sequence = 1;
            for (String imageUrl : imageUrls) {
                PostImage postImage = new PostImage();
                postImage.setPost(post);
                postImage.setImageUrl(imageUrl);
                postImage.setOrderSequence(sequence++);
                postImageRepository.save(postImage);
            }
        }

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
    // 게시글 목록 조회
    public List<PostPreviewResponseDto> getPostList() {
        List<Post> posts = postRepository.findAll(); // 모든 게시글 조회

        return posts.stream().map(post -> {
            // 대표 이미지 가져오기
            String previewImage = postImageRepository.findTopByPostOrderByOrderSequenceAsc(post)
                    .map(PostImage::getImageUrl)
                    .orElse(null); // 대표 이미지가 없으면 null 반환

            // 목록 데이터 구성
            PostPreviewResponseDto responseDto = new PostPreviewResponseDto();
            responseDto.setPostId(post.getId());
            responseDto.setTitle(post.getTitle());
            responseDto.setPrice(post.getPrice());
            responseDto.setPreviewImage(previewImage); // 대표 이미지 설정
            responseDto.setLocationName(post.getLocationName()); // 위치 정보 설정
            responseDto.setPostType(post.getType().name());
            return responseDto;
        }).collect(Collectors.toList());
    }

    public List<PostPreviewResponseDto> getMyPosts(Long userId) {
        log.info("Fetching posts for user: {}", userId);

        // 내가 쓴 게시글 조회
        List<Post> posts = postRepository.findByWriterId(userId);

        return posts.stream().map(post -> {
            // 대표 이미지 가져오기
            String previewImage = postImageRepository.findTopByPostOrderByOrderSequenceAsc(post)
                    .map(PostImage::getImageUrl)
                    .orElse(null); // 대표 이미지가 없으면 null 반환

            // 목록 데이터 구성
            PostPreviewResponseDto responseDto = new PostPreviewResponseDto();
            responseDto.setPostId(post.getId());
            responseDto.setTitle(post.getTitle());
            responseDto.setPrice(post.getPrice());
            responseDto.setPreviewImage(previewImage); // 대표 이미지 설정
            responseDto.setLocationName(post.getLocationName()); // 위치 정보
            responseDto.setPostType(post.getType().name());
            return responseDto;
        }).collect(Collectors.toList());
    }


}
