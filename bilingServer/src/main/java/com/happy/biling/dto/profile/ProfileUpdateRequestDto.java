package com.happy.biling.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;
    private String bio;

    public boolean hasNickname() {
        return nickname != null;
    }

    public boolean hasBio() {
        return bio != null;
    }
}

