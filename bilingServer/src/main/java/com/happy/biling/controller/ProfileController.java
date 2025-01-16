package com.happy.biling.controller;

import com.happy.biling.domain.service.ProfileService;
import com.happy.biling.dto.profile.CheerRequestDto;
import com.happy.biling.dto.profile.ProfileRequestDto;
import com.happy.biling.dto.profile.ProfileResponseDto;
import com.happy.biling.dto.profile.ProfileUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    // 타인의 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
        ProfileResponseDto profile = profileService.getProfile(requestDto);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequestDto requestDto) {
        ProfileResponseDto updatedProfile = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }

//    @PostMapping("/{userId}/cheers")
//    public ResponseEntity<Void> cheerUser(
//            @PathVariable Long userId,
//            @RequestBody CheerRequestDto requestDto) {
//        profileService.addCheer(requestDto);
//        return ResponseEntity.ok().build();
//    // 내 프로필 조회
//    @GetMapping("/me")
//    public ResponseEntity<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
//        Long userId = Long.parseLong(userDetails.getUsername());
//        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
//        ProfileResponseDto profile = profileService.getProfile(requestDto);
//        return ResponseEntity.ok(profile);
//    }
}