package com.happy.biling.controller;

import com.happy.biling.domain.service.UserService;
import com.happy.biling.domain.service.PostService;
import com.happy.biling.dto.auth.*;
import com.happy.biling.dto.post.PostPreviewResponseDto;
import com.happy.biling.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserConroller {
    
    private final UserService userService; 
    private final PostService postService;
    private final JwtUtil jwtUtil;
    
    @GetMapping("/auth/check-nickname")
    public ResponseEntity<Map<String, Boolean>> createPost(@RequestBody NicknameCheckRequestDto requestDto) {
        try {
        	boolean isDuplicate = userService.checkNickname(requestDto);
        	
            Map<String, Boolean> response = new HashMap<>();
            response.put("isDuplicate", isDuplicate);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<PostPreviewResponseDto>> getPostsByUser(
            @PathVariable("id") String userId,
            @RequestParam(value = "status", required = false) String status,
            @RequestHeader("Authorization") String authHeader) {

        try {
            Long userIdToFetch;

            // userId가 "me"이면 토큰에서 유저 ID 직접 추출
            if ("me".equals(userId)) {
                String token = authHeader.substring(7); // "Bearer " 제거
                userIdToFetch = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            } else {
                userIdToFetch = Long.valueOf(userId);
            }

            List<PostPreviewResponseDto> posts = postService.getPostsByUserId(userIdToFetch, status);
            return ResponseEntity.ok(posts);

        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/users/address/dong")
    public ResponseEntity<AddressResponseDto> getUserAddress(
            @RequestHeader("Authorization") String authHeader) {

    	Long userId;
    	
        try {
            String token = authHeader.substring(7); // "Bearer " 제거
            userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
                
            AddressResponseDto address = userService.getAddressByUserId(userId);
            return ResponseEntity.ok(address);

        } catch (Exception e) {
            log.error("Error fetching address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PatchMapping("/users/address")
    public ResponseEntity<AddressResponseDto> updateUserAddress(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateAddressRequestDto requestDto) {
    	Long userId;
    	
        try {
            String token = authHeader.substring(7); // "Bearer " 제거
            userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            userService.updateUserAddress(requestDto, userId);
                
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            log.error("Error fetching address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
