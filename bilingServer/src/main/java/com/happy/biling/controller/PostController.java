package com.happy.biling.controller;

import com.happy.biling.domain.service.PostService;
import com.happy.biling.dto.post.PostWriteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostWriteRequestDto requestDto) {
        try {
            postService.createPost(requestDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //성공
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //실패
        }
    }
}
