package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.entity.enums.PostStatus;
import com.happy.biling.domain.entity.enums.Tier;
import com.happy.biling.domain.repository.CheerRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.auth.AddressResponseDto;
import com.happy.biling.dto.auth.NicknameCheckRequestDto;
import com.happy.biling.dto.auth.SignUpRequestDto;
import com.happy.biling.dto.auth.UpdateAddressRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
	private final CheerRepository cheerRepository;
	private final PostRepository postRepository;

    // 중복 닉네임 검증
    public Boolean checkNickname(NicknameCheckRequestDto requestDto) {
    	return userRepository.existsByNickname(requestDto.getNickname());   
    }

	public Optional<User> findUserById(Long id) {
    	return userRepository.findById(id);   
	}
    
	public Optional<User> findUserByKakaoId(String kakaoId) {
    	return userRepository.findByKakaoId(kakaoId);   
	}

    public User createUser(SignUpRequestDto requestDto) {
		User user = new User();

		user.setKakaoId(requestDto.getKakaoId());
		user.setLocationLatitude(requestDto.getLocationLatitude());
		user.setLocationLongitude(requestDto.getLocationLongitude());
		user.setLocationName(requestDto.getLocationName());
		user.setNickname(requestDto.getNickName());

        return userRepository.save(user);
    }
    
    public AddressResponseDto getAddressByUserId(Long userId) {
    	AddressResponseDto responseDto = new AddressResponseDto();
    	
    	User user = userRepository.findById(userId)
    	            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    	
        String userLocationName = user.getLocationName();
        String[] splitAddress = userLocationName.split(" "); // 공백 기준으로 나눔
    	responseDto.setAddress(splitAddress[splitAddress.length - 1]); // 맨 마지막 단어(동)
    	
        return responseDto;
    }
    
    public User updateUserAddress(UpdateAddressRequestDto requestDto, Long userId) {
    	User user = userRepository.findById(userId)
    	            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    	
        user.setLocationName(requestDto.getLocationName());
        user.setLocationLatitude(requestDto.getLocationLatitude());
        user.setLocationLongitude(requestDto.getLocationLongitude());

        return userRepository.save(user);
    }

//	@Transactional
//	public void incrementRentalCount(Long userId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new RuntimeException("User not found"));
//
//		user.setRentalCount(user.getRentalCount() + 1); // rental_count 증가
//		userRepository.save(user);
//
//		updateUserTier(userId); // tier 업데이트
//	}

	@Transactional
	public void updateUserTier(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		int baseRentalCount = user.getRentalCount();
		long totalCheers = cheerRepository.countByReceiverId(userId);
		int additionalCount = (int) (totalCheers / 30);
		int totalRentalCount = baseRentalCount + additionalCount;

		TierInfo tierInfo = calculateTierInfo(totalRentalCount);
		user.setTier(tierInfo.getCurrentTier()); // tier 업데이트
		userRepository.save(user);
	}

	// ProfileService에서 사용할 메소드 추가
	public TierInfo calculateUserTierInfo(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		int totalRentalCount = calculateTotalRentalCount(userId);
		return calculateTierInfo(totalRentalCount);
	}

	public int calculateTotalRentalCount(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// 실제 거래완료 게시글 수를 조회
		long completedPostCount = postRepository.countByWriterIdAndStatus(userId, PostStatus.거래완료);

		// 응원수에 따른 추가 점수
		long totalCheers = cheerRepository.countByReceiverId(userId);
		int additionalCount = (int) (totalCheers / 30);

		return (int)completedPostCount + additionalCount;
	}
	@Transactional
	public void recalculateRentalCount(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// 해당 유저가 작성한 거래완료 상태의 게시글 수 조회
		long completedPostCount = postRepository.countByWriterIdAndStatus(userId, PostStatus.거래완료);

		// rental_count 업데이트
		user.setRentalCount((int) completedPostCount);
		userRepository.save(user);

		// tier도 함께 업데이트
		updateUserTier(userId);
	}

	public TierInfo calculateTierInfo(int totalCount) {
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

	@Getter
	@AllArgsConstructor
	public static class TierInfo {
		private final Tier currentTier;
		private final Tier nextTier;
		private final int remainingCount;
	}

}
