package com.happy.biling.dto.profile;

import com.happy.biling.domain.entity.enums.Tier;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ProfileResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String bio;
    private Tier tier;
    private String locationName;
    private Integer rentalCount;
    private Boolean allowNotification;
    private long cheerCount;
    private Tier nextTier;  // 다음 티어
    private int remainingCountToNextTier;  // 다음 티어까지 남은 건수
}