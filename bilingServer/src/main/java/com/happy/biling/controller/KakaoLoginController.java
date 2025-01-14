package com.happy.biling.controller;

import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.service.KaKaoService;
import com.happy.biling.domain.service.UserService;
import com.happy.biling.dto.auth.*;
import com.happy.biling.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.result.view.RedirectView;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoLoginController {
    
    private final UserService userService;
    private final KaKaoService kakaoService;  
    private final JwtUtil jwtUtil; // JwtUtil 의존성 주입

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    
    @GetMapping("/auth/kakao")
    public ResponseEntity<String> redirectToKakaoAuth() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" 
                              + client_id + "&redirect_uri=" + redirect_uri;

        // RestTemplate을 사용하여 요청
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(kakaoAuthUrl, String.class);

        return ResponseEntity.ok(response);
    }
    
    //카카오 로그인/회원가입
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String accessCode) {
        try {
            KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(accessCode);
            String accessToken = kakaoTokenResponseDto.getAccessToken();
            
            KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
            Optional<User> existUser = userService.findUserByKakaoId(userInfo.getId().toString());
            
            if (existUser.isEmpty()) {
            	// 회원 정보가 없으면 
            	KakaoLoginResponseDto kakaoLoginResponseDto = new KakaoLoginResponseDto();
            	kakaoLoginResponseDto.setKakaoId(userInfo.getId().toString());
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(kakaoLoginResponseDto);
            } else {
                // 회원 정보가 있으면 로그인 처리
            	User user = existUser.get();                
                
                String token = jwtUtil.generateToken(String.valueOf(user.getId()));
                TokenResponseDto responseDto = new TokenResponseDto();
                responseDto.setAccessToken(token);
              

                //TODO Refresh 토큰도 발급하도록 추후 수정 필요
                responseDto.setRefreshToken(token+"imsi");

                
                return ResponseEntity.ok(responseDto);
            }
                        
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/auth/kakao/sign-up")
    public ResponseEntity<?> addAdditionalInfo(@RequestBody SignUpRequestDto requestDto) {
        try {
        	User createdUser = userService.createUser(requestDto);
            
			String token = jwtUtil.generateToken(String.valueOf(createdUser.getId()));
            TokenResponseDto responseDto = new TokenResponseDto();
            responseDto.setAccessToken(token);
            //TODO Refresh 토큰도 발급하도록 추후 수정 필요
            responseDto.setRefreshToken(token+"imsi");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("추가 정보 입력 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    
}
