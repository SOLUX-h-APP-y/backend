package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.PostImage;
import com.happy.biling.domain.entity.Review;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.entity.enums.Category;
import com.happy.biling.domain.entity.enums.Distance;
import com.happy.biling.domain.entity.enums.PostStatus;
import com.happy.biling.domain.entity.enums.PostType;
import com.happy.biling.domain.repository.PostImageRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.ReviewRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.post.FilteredPostPreviewResponseDto;
import com.happy.biling.dto.post.PostDetailResponseDto;
import com.happy.biling.dto.post.PostPreviewResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;

    // 게시글 생성
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
    
    @Transactional
    public Post reuploadPost(Long postId, PostWriteRequestDto requestDto, Long userId) {
        Post previousPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 거래중 여부 확인
        if ("거래중".equals(previousPost.getStatus().name())) {
            boolean isSameContent = isSamePostContent(previousPost, requestDto);

            if (isSameContent /*&& isSameImages*/) {
                throw new IllegalStateException("It didn't modify");
            }
        }

        return createPost(requestDto, userId);
    }
    

    
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not the writer of this post");
        }

        if ("거래완료".equals(post.getStatus().name())) {
            throw new IllegalStateException("Cannot delete a post with status '거래완료'");
        }

        //TODO 포스트와 연관된 채팅이 있을 때도 삭제하면 안되나? 거래완료를 못 지우게 하면 리뷰 같은 건 자동적으로 고려하긴 함
        
        // postImage 삭제
        List<PostImage> postImages = postImageRepository.findByPost(post);
        if (!postImages.isEmpty()) {
            postImageRepository.deleteAll(postImages);
        }

        // 게시글 삭제
        postRepository.delete(post);
    }
    
    public void updatePostStatus(Long postId, Long userId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not the writer of this post");
        }
        
        PostStatus newStatus = PostStatus.valueOf(status);
        post.setStatus(newStatus);;
        
        postRepository.save(post);
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
        responseDto.setPostStatus(post.getStatus().name());
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
    
    // 특정 유저 게시글 조회
    public List<PostPreviewResponseDto> getPostsByUserId(Long userId, String status) {
        log.info("Fetching posts for user: {}, status: {}", userId, status);

        List<Post> posts = postRepository.findByWriterId(userId);

        if (status != null && !status.isEmpty()) {
            posts = posts.stream()
                    .filter(post -> status.equalsIgnoreCase(post.getStatus().name()))
                    .collect(Collectors.toList());
        }

        return posts.stream().map(post -> {
            // 대표 이미지 가져오기
            String previewImage = postImageRepository.findTopByPostOrderByOrderSequenceAsc(post)
                    .map(PostImage::getImageUrl)
                    .orElse(null);

            // postStatus가 "거래완료"인 경우 리뷰 조회
            Long reviewId = null;
            if ("거래완료".equals(post.getStatus().name())) {
                reviewId = reviewRepository.findByPostIdAndRevieweeId(post.getId(), userId)
                        .map(Review::getId)
                        .orElse(null);
            }

            PostPreviewResponseDto responseDto = new PostPreviewResponseDto();
            responseDto.setPostId(post.getId());
            responseDto.setTitle(post.getTitle());
            responseDto.setPrice(post.getPrice());
            responseDto.setPreviewImage(previewImage);
            responseDto.setLocationName(post.getLocationName());
            responseDto.setPostType(post.getType().name());
            responseDto.setPostStatus(post.getStatus().name());
            responseDto.setReviewId(reviewId);
            return responseDto;

        }).collect(Collectors.toList());
    }


    public List<FilteredPostPreviewResponseDto> getFilteredPosts(String type, String category, String radius, String keyword, Long userId) {
        log.info("Filtering posts with type: {}, category: {}, radius: {}, keyword: {}, userId: {}", type, category, radius, keyword, userId);

        // 현재 유저의 위치 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Double userLatitude = user.getLocationLatitude();
        Double userLongitude = user.getLocationLongitude();

        // 조건에 따라 게시글 필터링
        List<Post> filteredPosts = postRepository.findAll().stream()
                .filter(post -> post.getType().name().equalsIgnoreCase(type)) // type 필터
                .filter(post -> isValidCategory(post.getCategory(), category)) // category 필터
                .filter(post -> isWithinRadius(post.getLocationLatitude(), post.getLocationLongitude(), userLatitude, userLongitude, radius)) // 거리 필터
                .filter(post -> isValidKeyword(post.getTitle(), keyword)) // keyword 필터
                .filter(post -> post.getExpirationDate().isAfter(LocalDateTime.now())) // 유효한 게시글
                .collect(Collectors.toList());

        // 게시글 목록을 DTO로 변환
        return filteredPosts.stream().map(post -> {
            String previewImage = postImageRepository.findTopByPostOrderByOrderSequenceAsc(post)
                    .map(PostImage::getImageUrl)
                    .orElse(null);

            FilteredPostPreviewResponseDto responseDto = new FilteredPostPreviewResponseDto();
            responseDto.setPostId(post.getId());
            responseDto.setTitle(post.getTitle());
            responseDto.setPrice(post.getPrice());
            responseDto.setPreviewImage(previewImage);
            responseDto.setLocationName(post.getLocationName());
            
            return responseDto;
        }).collect(Collectors.toList());
    }

    
    //post 관련 유틸성 함수 모음
    private boolean isSamePostContent(Post previousPost, PostWriteRequestDto requestDto) {
        return previousPost.getType().name().equals(requestDto.getType()) &&
               previousPost.getTitle().equals(requestDto.getTitle()) &&
               previousPost.getPrice().equals(requestDto.getPrice()) &&
               previousPost.getContent().equals(requestDto.getContent()) &&
               previousPost.getDistance().name().equals(requestDto.getDistance()) &&
               previousPost.getCategory().name().equals(requestDto.getCategory()) &&
               previousPost.getLocationName().equals(requestDto.getLocationName()) &&
               previousPost.getLocationLatitude().equals(requestDto.getLocationLatitude()) &&
               previousPost.getLocationLongitude().equals(requestDto.getLocationLongitude());
    }

    private boolean isValidCategory(Category postCategory, String requestedCategory) {
        return requestedCategory == null || requestedCategory.equalsIgnoreCase("전체") || postCategory.name().equalsIgnoreCase(requestedCategory);
    }

    private boolean isWithinRadius(Double postLat, Double postLon, Double userLat, Double userLon, String radius) {
        if (radius == null || radius.equalsIgnoreCase("거리무관")) return true;

        double distance = calculateDistance(userLat, userLon, postLat, postLon);
        int radiusValue = Integer.parseInt(radius.replace("km", "")); // 3km, 5km, 10km 처리
        return distance <= radiusValue;
    }

    private boolean isValidKeyword(String postTitle, String keyword) {
        return keyword == null || postTitle.toLowerCase().contains(keyword.toLowerCase());
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반지름(km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
