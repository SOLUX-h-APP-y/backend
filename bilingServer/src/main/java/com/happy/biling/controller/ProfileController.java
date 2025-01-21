package com.happy.biling.controller;

import com.happy.biling.domain.service.ProfileService;
import com.happy.biling.dto.profile.CheerRequestDto;
import com.happy.biling.dto.profile.ProfileRequestDto;
import com.happy.biling.dto.profile.ProfileResponseDto;
import com.happy.biling.dto.profile.ProfileUpdateRequestDto;
import com.happy.biling.security.JwtAuthentication;
import com.happy.biling.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    // 자신의 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyProfile() {
        Long userId = getCurrentUserId();
        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
        ProfileResponseDto profile = profileService.getProfile(requestDto);
        return ResponseEntity.ok(profile);
    }

    // 타인의 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
        ProfileResponseDto profile = profileService.getProfile(requestDto);
        return ResponseEntity.ok(profile);
    }

    // 프로필 수정 - 자신의 프로필만 수정 가능
    @PatchMapping("/me")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @RequestBody ProfileUpdateRequestDto requestDto) {
        Long userId = getCurrentUserId();
        ProfileResponseDto updatedProfile = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }

    // 응원하기
    @PostMapping("/{receiverId}/cheers")
    public ResponseEntity<Void> addCheer(@PathVariable Long receiverId) {
        Long senderId = getCurrentUserId();
        CheerRequestDto requestDto = new CheerRequestDto(senderId, receiverId);
        profileService.addCheer(requestDto);
        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            String userIdStr = (String) authentication.getPrincipal();
            return Long.parseLong(userIdStr);
        }
        throw new RuntimeException("Not authenticated");
    }
}
