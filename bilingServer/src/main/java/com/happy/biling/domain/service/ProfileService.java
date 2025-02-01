//package com.happy.biling.domain.service;
//
//import com.happy.biling.domain.entity.Cheer;
//import com.happy.biling.domain.entity.User;
//import com.happy.biling.domain.repository.CheerRepository;
//import com.happy.biling.domain.repository.PostRepository;
//import com.happy.biling.domain.repository.UserRepository;
//import com.happy.biling.dto.profile.CheerRequestDto;
//import com.happy.biling.dto.profile.ProfileRequestDto;
//import com.happy.biling.dto.profile.ProfileResponseDto;
//import com.happy.biling.dto.profile.ProfileUpdateRequestDto;
//import com.happy.biling.domain.entity.enums.PostStatus;
//import com.happy.biling.domain.entity.enums.Tier;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class ProfileService {
//    private final UserRepository userRepository;
//    //private final PostRepository postRepository;
//    private final CheerRepository cheerRepository;
//    private final S3Uploader s3Uploader;
//
//    @Getter
//    @AllArgsConstructor
//    private static class TierInfo {
//        private final Tier currentTier;
//        private final Tier nextTier;
//        private final int remainingCount;
//    }
//
//    // 프로필 조회
//    public ProfileResponseDto getProfile(ProfileRequestDto requestDto) {
//        User user = userRepository.findById(requestDto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 실제 거래 완료 횟수 조회
//        int baseRentalCount = user.getRentalCount();
//
//        // 총 응원 횟수 조회
//        long totalCheers = cheerRepository.countByReceiverId(user.getId());
//
//        // 응원으로 인한 추가 카운트 (30개당 1회 추가)
//        int additionalCount = (int) (totalCheers / 30);
//
//        // 최종 대여 횟수 = 기존 rentalCount + 응원으로 인한 추가 횟수
//        int totalRentalCount = baseRentalCount + additionalCount;
//
//        // 티어 정보 계산
//        TierInfo tierInfo = calculateTierInfo(totalRentalCount);
//
//        return ProfileResponseDto.builder()
//                .userId(user.getId())
//                .nickname(user.getNickname())
//                .profileImage(user.getProfileImage())
//                .bio(user.getBio())
//                .tier(tierInfo.getCurrentTier())
//                .nextTier(tierInfo.getNextTier())
//                .remainingCountToNextTier(tierInfo.getRemainingCount())
//                .locationName(user.getLocationName())
//                .rentalCount(totalRentalCount)
//                .cheerCount(totalCheers)  // 총 응원 수 포함
//                .allowNotification(user.getAllowNotification())
//                .build();
//    }
//
//        //프로필 수정
//    @Transactional
//    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto requestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 닉네임 수정
//        if (requestDto.hasNickname()) {
//            if (!user.getNickname().equals(requestDto.getNickname())
//                    && userRepository.existsByNickname(requestDto.getNickname())) {
//                throw new RuntimeException("이미 사용 중인 닉네임입니다.");
//            }
//            user.setNickname(requestDto.getNickname());
//        }
//
//        // 프로필 이미지 수정
//        if (requestDto.hasProfileImage()) {
//            user.setProfileImage(requestDto.getProfileImage());
//        }
//
//        // 자기소개 수정
//        if (requestDto.hasBio()) {
//            user.setBio(requestDto.getBio());
//        }
//
//        // 총 응원 횟수 조회
//        long totalCheers = cheerRepository.countByReceiverId(userId);
//
//        // 응원으로 인한 추가 카운트 (30개당 1회 추가)
//        int additionalCount = (int) (totalCheers / 30);
//
//        // 최종 대여 횟수 계산
//        int totalRentalCount = user.getRentalCount() + additionalCount;
//
//        // 티어 정보 계산
//        TierInfo tierInfo = calculateTierInfo(totalRentalCount);
//
//        return ProfileResponseDto.builder()
//                .userId(user.getId())
//                .nickname(user.getNickname())
//                .profileImage(user.getProfileImage())
//                .bio(user.getBio())
//                .tier(tierInfo.getCurrentTier())
//                .nextTier(tierInfo.getNextTier())
//                .remainingCountToNextTier(tierInfo.getRemainingCount())
//                .locationName(user.getLocationName())
//                .rentalCount(totalRentalCount)
//                .cheerCount(totalCheers)  // 총 응원 수 포함
//                .allowNotification(user.getAllowNotification())
//                .build();
//    }
//
//
//
//    // 응원 추가
//    @Transactional
//    public void addCheer(CheerRequestDto requestDto) {
//        // 중복 응원 체크
//        if (cheerRepository.existsBySenderIdAndReceiverId(
//                requestDto.getSenderId(),
//                requestDto.getReceiverId())) {
//            throw new RuntimeException("이미 응원한 사용자입니다.");
//        }
//
//        // 자기 자신 응원 방지
//        if (requestDto.getSenderId().equals(requestDto.getReceiverId())) {
//            throw new RuntimeException("자기 자신은 응원할 수 없습니다.");
//        }
//
//        // 현재 수신자의 총 응원 수 조회
//        long currentTotalCheers = cheerRepository.countByReceiverId(requestDto.getReceiverId());
//
//        // 새로운 응원 생성 및 저장
//        Cheer cheer = new Cheer();
//        cheer.setSenderId(requestDto.getSenderId());
//        cheer.setReceiverId(requestDto.getReceiverId());
//        cheer.setTotalCheers((int) (currentTotalCheers + 1));  // 총 응원 수 업데이트
//        cheerRepository.save(cheer);
//    }
//
//    // 티어 정보 계산
//    private TierInfo calculateTierInfo(int totalCount) {
//        if (totalCount >= 100) {
//            return new TierInfo(Tier.지구, null, 0);
//        }
//        if (totalCount >= 80) {
//            return new TierInfo(Tier.숲, Tier.지구, 100 - totalCount);
//        }
//        if (totalCount >= 60) {
//            return new TierInfo(Tier.나무, Tier.숲, 80 - totalCount);
//        }
//        if (totalCount >= 30) {
//            return new TierInfo(Tier.풀, Tier.나무, 60 - totalCount);
//        }
//        if (totalCount >= 10) {
//            return new TierInfo(Tier.새싹, Tier.풀, 30 - totalCount);
//        }
//        return new TierInfo(Tier.씨앗, Tier.새싹, 10 - totalCount);
//    }
//}
package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Cheer;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.CheerRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.profile.*;
import com.happy.biling.domain.entity.enums.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final CheerRepository cheerRepository;
    private final S3Uploader s3Uploader;
    private static final String DEFAULT_PROFILE_IMAGE = "https://biling-img11-bucket.s3.ap-northeast-2.amazonaws.com/profile/Group+849.jpg";

    // 프로필 조회
    public ProfileResponseDto getProfile(ProfileRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String profileImage = user.getProfileImage();
        if (profileImage == null || profileImage.isEmpty()) {
            profileImage = DEFAULT_PROFILE_IMAGE;
        }

        UserService.TierInfo tierInfo = userService.calculateUserTierInfo(user.getId());
        int totalRentalCount = userService.calculateTotalRentalCount(user.getId());
        long totalCheers = cheerRepository.countByReceiverId(user.getId());

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(profileImage)
                .bio(user.getBio())
                .tier(tierInfo.getCurrentTier())
                .nextTier(tierInfo.getNextTier())
                .remainingCountToNextTier(tierInfo.getRemainingCount())
                .locationName(user.getLocationName())
                .rentalCount(totalRentalCount)
                .cheerCount(totalCheers)
                .allowNotification(user.getAllowNotification())
                .build();
    }

    // 프로필 수정 (MultipartFile 포함)
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto requestDto, MultipartFile profileImageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 닉네임 수정
        if (requestDto.hasNickname()) {
            if (!user.getNickname().equals(requestDto.getNickname())
                    && userRepository.existsByNickname(requestDto.getNickname())) {
                throw new RuntimeException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(requestDto.getNickname());
        }

        // 프로필 이미지 삭제 후 업로드
        String profileImage = user.getProfileImage();
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            log.info("Uploading new profile image...");
            if (profileImage != null) {
                s3Uploader.deleteFile(profileImage);
            }
            profileImage = s3Uploader.uploadFile(profileImageFile, "profile");
            user.setProfileImage(profileImage);
        }

        if (profileImage == null || profileImage.isEmpty()) {
            profileImage = DEFAULT_PROFILE_IMAGE;
        }

        // 자기소개 수정
        if (requestDto.hasBio()) {
            user.setBio(requestDto.getBio());
        }

        UserService.TierInfo tierInfo = userService.calculateUserTierInfo(userId);
        int totalRentalCount = userService.calculateTotalRentalCount(userId);
        long totalCheers = cheerRepository.countByReceiverId(userId);

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(profileImage)
                .bio(user.getBio())
                .tier(tierInfo.getCurrentTier())
                .nextTier(tierInfo.getNextTier())
                .remainingCountToNextTier(tierInfo.getRemainingCount())
                .locationName(user.getLocationName())
                .rentalCount(totalRentalCount)
                .cheerCount(totalCheers)
                .allowNotification(user.getAllowNotification())
                .build();
    }

    // 응원 추가
    @Transactional
    public CheerResponseDto addCheer(CheerRequestDto requestDto) {
        // 중복 응원 체크
        if (cheerRepository.existsBySenderIdAndReceiverId(
                requestDto.getSenderId(),
                requestDto.getReceiverId())) {
            throw new RuntimeException("이미 응원한 사용자입니다.");
        }
        // 자기 자신 응원 방지
        if (requestDto.getSenderId().equals(requestDto.getReceiverId())) {
            throw new RuntimeException("자기 자신은 응원할 수 없습니다.");
        }

        // 현재 수신자의 총 응원 수 조회
        long currentTotalCheers = cheerRepository.countByReceiverId(requestDto.getReceiverId());

        // 새로운 응원 생성 및 저장
        Cheer cheer = new Cheer();
        cheer.setSenderId(requestDto.getSenderId());
        cheer.setReceiverId(requestDto.getReceiverId());
        cheer.setTotalCheers((int) (currentTotalCheers + 1));
        cheerRepository.save(cheer);

        // 응원 추가 후 tier 업데이트
        userService.updateUserTier(requestDto.getReceiverId());

        return new CheerResponseDto(currentTotalCheers + 1);
    }
}