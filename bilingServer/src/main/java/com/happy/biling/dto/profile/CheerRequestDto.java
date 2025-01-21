package com.happy.biling.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CheerRequestDto {
    private Long senderId;
    private Long receiverId;
}

