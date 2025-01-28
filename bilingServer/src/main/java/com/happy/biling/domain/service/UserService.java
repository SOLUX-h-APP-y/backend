package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.auth.AddressResponseDto;
import com.happy.biling.dto.auth.NicknameCheckRequestDto;
import com.happy.biling.dto.auth.SignUpRequestDto;
import com.happy.biling.dto.auth.UpdateAddressRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

}
