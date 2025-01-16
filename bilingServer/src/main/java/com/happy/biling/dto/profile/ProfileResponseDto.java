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
}