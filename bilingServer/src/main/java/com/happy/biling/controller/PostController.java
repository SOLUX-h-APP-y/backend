package com.happy.biling.controller;

import com.happy.biling.domain.service.KaKaoService;
import com.happy.biling.domain.service.PostService;
//import com.happy.biling.dto.post.PostDetailResponseDto;
import com.happy.biling.dto.post.PostWriteRequestDto;
import com.happy.biling.security.JwtUtil;
import org.slf4j.*;
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
    
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(
    		@RequestHeader("Authorization") String authHeader,
    		@RequestBody PostWriteRequestDto requestDto) {
    
        try {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
        	Long userId = Long.valueOf(JwtUtil.getUserIdFromToken(token));
        	log.info("userId : ",userId.toString());
        	requestDto.setWriterId(userId);
            postService.createPost(requestDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //성공
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
        }
    }
    /*
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponseDto> readPostDetail(
    		@PathVariable long postId,
    		@RequestParam(value = "userId", required = false) Long userId) {
        try {
            PostDetailResponseDto responseDto = postService.readPostDetail(postId, userId);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    */
}
