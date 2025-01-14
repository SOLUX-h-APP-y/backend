package com.happy.biling.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
	private String kakaoId;
	private String nickName;
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
}