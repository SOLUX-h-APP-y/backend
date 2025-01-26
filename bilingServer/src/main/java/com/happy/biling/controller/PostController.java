package com.happy.biling.controller;

import com.happy.biling.domain.service.PostService;
import com.happy.biling.dto.post.PostDetailResponseDto;
import com.happy.biling.dto.post.PostPreviewResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import com.happy.biling.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    //    @PostMapping("/posts")
//    public ResponseEntity<Void> createPost(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody PostWriteRequestDto requestDto) {
//
//        try {
//            String token = authHeader.substring(7); //"Bearer " 이후의 토큰 추출
//            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            postService.createPost(requestDto, userId);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //성공
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
//        }
//    }

    //    @GetMapping("/posts/{id}")
//    public ResponseEntity<PostDetailResponseDto> getPostDetail(
//            @PathVariable("id") Long id,
//            @RequestHeader("Authorization") String authHeader) {
//
//        try {
//            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
//            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            PostDetailResponseDto responseDto = postService.getPostDetail(id, userId);
//            return ResponseEntity.ok(responseDto);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
//        }
//    }
// 게시글 생성
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPost(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute PostWriteRequestDto requestDto) {

        log.info("Entering createPost method. Authorization Header: {}, Request DTO: {}", authHeader, requestDto);

        try {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("Extracted User ID: {}", userId);

            postService.createPost(requestDto, userId);
            log.info("Post creation completed.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 성공
        } catch (Exception e) {
            log.error("Error during post creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 실패
        }
    }

    // 게시글 상세 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            PostDetailResponseDto responseDto = postService.getPostDetail(id, userId);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 실패
        }
    }
    // 게시글 목록 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostPreviewResponseDto>> getPostList() {
        List<PostPreviewResponseDto> postList = postService.getPostList();
        return ResponseEntity.ok(postList);
    }
    // 내가 쓴 게시글 목록 조회
    @GetMapping("/posts/me")
    public ResponseEntity<List<PostPreviewResponseDto>> getMyPosts(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("Fetching posts for User ID: {}", userId);

            List<PostPreviewResponseDto> postList = postService.getMyPosts(userId);
            return ResponseEntity.ok(postList);
        } catch (Exception e) {
            log.error("Error while fetching user's posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 실패
        }
    }



}
