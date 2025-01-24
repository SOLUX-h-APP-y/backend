package com.happy.biling.controller;

import com.happy.biling.domain.service.PostService;
import com.happy.biling.dto.post.PostDetailResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import com.happy.biling.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostWriteRequestDto requestDto) {

        try {
            String token = authHeader.substring(7); //"Bearer " 이후의 토큰 추출
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            postService.createPost(requestDto, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //성공
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
        }
    }
    
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
        }
    }
}
