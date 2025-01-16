package com.happy.biling.controller;

import com.happy.biling.domain.service.UserService;
import com.happy.biling.dto.auth.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserConroller {
    
    private final UserService userService; 
    
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

    @GetMapping("/user/info")
    public String getUserInfo() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello, user with ID: " + userId;
    }

}
