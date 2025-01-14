package com.happy.biling.domain.service;

import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.auth.NicknameCheckRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 중복 닉네임 검증
    public Boolean checkNickname(NicknameCheckRequestDto requestDto) {
    	return userRepository.existsByNickname(requestDto.getNickname());   
    }
}
