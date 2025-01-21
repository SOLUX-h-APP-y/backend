package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.Cheer;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.CheerRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.profile.CheerRequestDto;
import com.happy.biling.dto.profile.ProfileRequestDto;
import com.happy.biling.dto.profile.ProfileResponseDto;
import com.happy.biling.dto.profile.ProfileUpdateRequestDto;
import com.happy.biling.domain.entity.enums.PostStatus;
import com.happy.biling.domain.entity.enums.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CheerRepository cheerRepository;

    // 티어 정보를 담는 내부 클래스
    @Getter
    @AllArgsConstructor
    private static class TierInfo {
        private final Tier currentTier;
        private final Tier nextTier;
        private final int remainingCount;
    }

    // 프로필 조회
    public ProfileResponseDto getProfile(ProfileRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 실제 거래 완료 횟수 조회 (DB에 저장된 값 사용)
        int baseRentalCount = user.getRentalCount();

        // 총 응원 횟수 조회
        long totalCheers = cheerRepository.countByReceiverId(user.getId());

        // 응원으로 인한 추가 카운트 (100개당 1회 추가)
        int additionalCount = (int) (totalCheers / 30);
        // 최종 대여 횟수 = 기존 rentalCount + 응원으로 인한 추가 횟수
        int totalRentalCount = baseRentalCount + additionalCount;

        // 티어 정보 계산
        TierInfo tierInfo = calculateTierInfo(totalRentalCount);

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
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

    // 응원 횟수 조회
    public long getTotalCheersByReceiverId(Long receiverId) {
        return cheerRepository.countByReceiverId(receiverId);
    }

    // 프로필 수정
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (requestDto.hasNickname()) {
            if (!user.getNickname().equals(requestDto.getNickname())
                    && userRepository.existsByNickname(requestDto.getNickname())) {
                throw new RuntimeException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(requestDto.getNickname());
        }

        if (requestDto.hasProfileImage()) {
            user.setProfileImage(requestDto.getProfileImage());
        }

        if (requestDto.hasBio()) {
            user.setBio(requestDto.getBio());
        }

        TierInfo tierInfo = calculateTierInfo(user.getRentalCount());

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .tier(tierInfo.getCurrentTier())
                .nextTier(tierInfo.getNextTier())
                .remainingCountToNextTier(tierInfo.getRemainingCount())
                .locationName(user.getLocationName())
                .rentalCount(user.getRentalCount())
                .allowNotification(user.getAllowNotification())
                .build();
    }

    // 응원 추가
    @Transactional
    public void addCheer(CheerRequestDto requestDto) {
        if (cheerRepository.existsBySenderIdAndReceiverId(
                requestDto.getSenderId(),
                requestDto.getReceiverId())) {
            throw new RuntimeException("이미 응원한 사용자입니다.");
        }

        if (requestDto.getSenderId().equals(requestDto.getReceiverId())) {
            throw new RuntimeException("자기 자신은 응원할 수 없습니다.");
        }

        Cheer cheer = new Cheer();
        cheer.setSenderId(requestDto.getSenderId());
        cheer.setReceiverId(requestDto.getReceiverId());
        cheerRepository.save(cheer);
    }

    // 티어 정보 계산
    private TierInfo calculateTierInfo(int totalCount) {
        if (totalCount >= 100) {
            return new TierInfo(Tier.지구, null, 0);
        }
        if (totalCount >= 80) {
            return new TierInfo(Tier.숲, Tier.지구, 100 - totalCount);
        }
        if (totalCount >= 60) {
            return new TierInfo(Tier.나무, Tier.숲, 80 - totalCount);
        }
        if (totalCount >= 30) {
            return new TierInfo(Tier.풀, Tier.나무, 60 - totalCount);
        }
        if (totalCount >= 10) {
            return new TierInfo(Tier.새싹, Tier.풀, 30 - totalCount);
        }
        return new TierInfo(Tier.씨앗, Tier.새싹, 10 - totalCount);
    }
}