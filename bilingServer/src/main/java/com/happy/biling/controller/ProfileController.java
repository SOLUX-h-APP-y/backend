//package com.happy.biling.controller;
//
//import com.happy.biling.domain.service.ProfileService;
//import com.happy.biling.dto.profile.CheerRequestDto;
//import com.happy.biling.dto.profile.ProfileRequestDto;
//import com.happy.biling.dto.profile.ProfileResponseDto;
//import com.happy.biling.dto.profile.ProfileUpdateRequestDto;
//import com.happy.biling.security.JwtAuthentication;
//import com.happy.biling.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
////@RestController
////@RequiredArgsConstructor
////@RequestMapping("/profiles")
////public class ProfileController {
////    private final ProfileService profileService;
////    private final JwtUtil jwtUtil;
////
////    // 자신의 프로필 조회
////    @GetMapping("/me")
////    public ResponseEntity<ProfileResponseDto> getMyProfile() {
////        Long userId = getCurrentUserId();
////        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
////        ProfileResponseDto profile = profileService.getProfile(requestDto);
////        return ResponseEntity.ok(profile);
////    }
////
////    // 타인의 프로필 조회
////    @GetMapping("/{userId}")
////    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
////        ProfileRequestDto requestDto = new ProfileRequestDto(userId);
////        ProfileResponseDto profile = profileService.getProfile(requestDto);
////        return ResponseEntity.ok(profile);
////    }
////
////    // 프로필 수정 - 자신의 프로필만 수정 가능
////    @CrossOrigin(origins = "*", methods = {RequestMethod.PATCH})
////    @PatchMapping("/me")
////    public ResponseEntity<ProfileResponseDto> updateProfile(
////            @RequestBody ProfileUpdateRequestDto requestDto) {
////        Long userId = getCurrentUserId();
////        ProfileResponseDto updatedProfile = profileService.updateProfile(userId, requestDto);
////        return ResponseEntity.ok(updatedProfile);
////    }
////
////    // 응원하기
////    @CrossOrigin(origins = "*", methods = {RequestMethod.POST})
////    @PostMapping("/{receiverId}/cheers")
////    public ResponseEntity<Void> addCheer(@PathVariable Long receiverId) {
////        Long senderId = getCurrentUserId();
////        CheerRequestDto requestDto = new CheerRequestDto(senderId, receiverId);
////        profileService.addCheer(requestDto);
////        return ResponseEntity.ok().build();
////    }
////
////    private Long getCurrentUserId() {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if (authentication instanceof JwtAuthentication) {
////            String userIdStr = (String) authentication.getPrincipal();
////            return Long.parseLong(userIdStr);
////        }
////        throw new RuntimeException("Not authenticated");
////    }
////}
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/profiles")
//@Slf4j
//public class ProfileController {
//    private final ProfileService profileService;
//    private final JwtUtil jwtUtil;
//
//    // 자신의 프로필 조회
//    @GetMapping("/me")
//    public ResponseEntity<ProfileResponseDto> getMyProfile(
//            @RequestHeader("Authorization") String authHeader) {
//        try {
//            String token = authHeader.substring(7);
//            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            log.info("userId : {}", userId);
//
//            ProfileRequestDto requestDto = new ProfileRequestDto(userId);
//            ProfileResponseDto profile = profileService.getProfile(requestDto);
//            return ResponseEntity.ok(profile);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // 타인의 프로필 조회
//    @GetMapping("/{userId}")
//    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
//        try {
//            ProfileRequestDto requestDto = new ProfileRequestDto(userId);
//            ProfileResponseDto profile = profileService.getProfile(requestDto);
//            return ResponseEntity.ok(profile);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // 프로필 수정 - 자신의 프로필만 수정 가능
//    @CrossOrigin(origins = "*", methods = {RequestMethod.PATCH})
//    @PatchMapping("/me")
//    public ResponseEntity<ProfileResponseDto> updateProfile(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody ProfileUpdateRequestDto requestDto) {
//        try {
//            String token = authHeader.substring(7);
//            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            log.info("userId : {}", userId);
//
//            ProfileResponseDto updatedProfile = profileService.updateProfile(userId, requestDto);
//            return ResponseEntity.ok(updatedProfile);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // 응원하기
//    @CrossOrigin(origins = "*", methods = {RequestMethod.POST})
//    @PostMapping("/{receiverId}/cheers")
//    public ResponseEntity<Void> addCheer(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long receiverId) {
//        try {
//            String token = authHeader.substring(7);
//            Long senderId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            log.info("senderId : {}", senderId);
//
//            CheerRequestDto requestDto = new CheerRequestDto(senderId, receiverId);
//            profileService.addCheer(requestDto);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
package com.happy.biling.controller;

import com.happy.biling.domain.service.ProfileService;
import com.happy.biling.dto.profile.*;
import com.happy.biling.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    // 자신의 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("userId : {}", userId);

            ProfileRequestDto requestDto = new ProfileRequestDto(userId);
            ProfileResponseDto profile = profileService.getProfile(requestDto);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            log.error("Error retrieving profile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 타인의 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        try {
            ProfileRequestDto requestDto = new ProfileRequestDto(userId);
            ProfileResponseDto profile = profileService.getProfile(requestDto);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            log.error("Error retrieving profile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로필 수정 - 자신의 프로필만 수정 가능
    @PatchMapping("/me")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            //@RequestBody ProfileUpdateRequestDto requestDto, // JSON 데이터를 매핑
            @ModelAttribute ProfileUpdateRequestDto requestDto,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        try {
            log.info("Received update profile request.");
            log.info("Nickname in DTO: {}", requestDto.getNickname()); // 닉네임 로깅
            log.info("Bio in DTO: {}", requestDto.getBio());           // 바이오 로깅
            log.info("Profile image file: {}",
                    profileImageFile != null ? profileImageFile.getOriginalFilename() : "No File"); // 이미지 파일 로깅
            String token = authHeader.substring(7);
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
            log.info("userId : {}", userId);

            ProfileResponseDto updatedProfile = profileService.updateProfile(userId, requestDto, profileImageFile);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            log.error("Error updating profile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    // 응원하기
//    @PostMapping("/{receiverId}/cheers")
//    public ResponseEntity<Void> addCheer(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long receiverId) {
//        try {
//            String token = authHeader.substring(7);
//            Long senderId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
//            log.info("senderId : {}", senderId);
//
//            CheerRequestDto requestDto = new CheerRequestDto(senderId, receiverId);
//            profileService.addCheer(requestDto);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } catch (Exception e) {
//            log.error("Error adding cheer: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
@PostMapping("/{receiverId}/cheers")
public ResponseEntity<CheerResponseDto> addCheer(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long receiverId) {
    try {
        String token = authHeader.substring(7);
        Long senderId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        CheerRequestDto requestDto = new CheerRequestDto(senderId, receiverId);
        CheerResponseDto response = profileService.addCheer(requestDto);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        log.error("Error adding cheer: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
