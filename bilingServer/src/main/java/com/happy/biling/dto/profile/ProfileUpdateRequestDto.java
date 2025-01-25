package com.happy.biling.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@NoArgsConstructor
//public class ProfileUpdateRequestDto {
//    private String nickname;
//    private String profileImage;
//    private String bio;
//
//    // 각 필드가 수정 요청에 포함되었는지 확인하는 메서드들
//    public boolean hasNickname() {
//        return nickname != null;
//    }
//
//    public boolean hasProfileImage() {
//        return profileImage != null;
//    }
//
//    public boolean hasBio() {
//        return bio != null;
//    }
//}
//@Getter
//@NoArgsConstructor
//public class ProfileUpdateRequestDto {
//    private String nickname;
//    private String bio;
//
//    public boolean hasNickname() {
//        return nickname != null;
//    }
//
//    public boolean hasBio() {
//        return bio != null;
//    }
//}
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

