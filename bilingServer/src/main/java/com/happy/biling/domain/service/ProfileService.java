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

    // 프로필 조회
    public ProfileResponseDto getProfile(ProfileRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 실제 거래 완료 횟수 조회 (DB에 저장된 값 사용)
        int baseRentalCount = user.getRentalCount(); // 기존 데이터베이스 값

        // 총 응원 횟수 조회
        long totalCheers = cheerRepository.countByReceiverId(user.getId());

        // 응원으로 인한 추가 카운트 (100개당 1회 추가)
        int additionalCount = (int) (totalCheers / 100);
        // 최종 대여 횟수 = 기존 rentalCount + 응원으로 인한 추가 횟수
        int totalRentalCount = baseRentalCount + additionalCount;

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .tier(calculateTier(totalRentalCount)) // 티어 계산
                .locationName(user.getLocationName())
                .rentalCount(totalRentalCount) // 총 대여 횟수
                .cheerCount(totalCheers) // 총 응원 횟수
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

        // 닉네임 업데이트 (요청에 포함된 경우에만)
        if (requestDto.hasNickname()) {
            if (!user.getNickname().equals(requestDto.getNickname())
                    && userRepository.existsByNickname(requestDto.getNickname())) {
                throw new RuntimeException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(requestDto.getNickname());
        }

        // 프로필 이미지 업데이트 (요청에 포함된 경우에만)
        if (requestDto.hasProfileImage()) {
            user.setProfileImage(requestDto.getProfileImage());
        }

        // 소개글 업데이트 (요청에 포함된 경우에만)
        if (requestDto.hasBio()) {
            user.setBio(requestDto.getBio());
        }

        return ProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .tier(user.getTier())
                .locationName(user.getLocationName())
                .rentalCount(user.getRentalCount())
                .allowNotification(user.getAllowNotification())
                .build();
    }

    // 응원 추가
    @Transactional
    public void addCheer(CheerRequestDto requestDto) {
        // 중복 응원 체크
        if (cheerRepository.existsBySenderIdAndReceiverId(
                requestDto.getSenderId(),
                requestDto.getReceiverId())) {
            throw new RuntimeException("이미 응원한 사용자입니다.");
        }

        // 자기 자신 응원 체크
        if (requestDto.getSenderId().equals(requestDto.getReceiverId())) {
            throw new RuntimeException("자기 자신은 응원할 수 없습니다.");
        }

        // 새로운 응원 생성
        Cheer cheer = new Cheer();
        cheer.setSenderId(requestDto.getSenderId());
        cheer.setReceiverId(requestDto.getReceiverId());
        cheerRepository.save(cheer);
    }

    // 티어 계산
    private Tier calculateTier(int totalCount) {
        if (totalCount >= 100) return Tier.지구;
        if (totalCount >= 80) return Tier.숲;
        if (totalCount >= 60) return Tier.나무;
        if (totalCount >= 30) return Tier.풀;
        if (totalCount >= 10) return Tier.새싹;
        return Tier.씨앗;
    }
}
