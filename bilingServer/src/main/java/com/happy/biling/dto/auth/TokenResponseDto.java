package com.happy.biling.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}