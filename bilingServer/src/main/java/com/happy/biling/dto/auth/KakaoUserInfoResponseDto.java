package com.happy.biling.dto.auth;

import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor //������ȭ�� ���� �⺻ ������
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {

    //ȸ�� ��ȣ
    @JsonProperty("id")
    public Long id;

    //�ڵ� ���� ������ ��Ȱ��ȭ�� ��츸 ����.
    //true : ���� ����, false : ���� ��� ����
    @JsonProperty("has_signed_up")
    public Boolean hasSignedUp;

    //���񽺿� ���� �Ϸ�� �ð�. UTC
    @JsonProperty("connected_at")
    public Date connectedAt;

    //īī����ũ �������� ���� �α����� �ð�. UTC
    @JsonProperty("synched_at")
    public Date synchedAt;

    //����� ������Ƽ
    @JsonProperty("properties")
    public HashMap<String, String> properties;

    //īī�� ���� ����
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    //uuid �� �߰� ����
    @JsonProperty("for_partner")
    public Partner partner;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {

        //������ ���� ���� ���� ����
        @JsonProperty("profile_needs_agreement")
        public Boolean isProfileAgree;

        //�г��� ���� ���� ����
        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean isNickNameAgree;

        //������ ���� ���� ���� ����
        @JsonProperty("profile_image_needs_agreement")
        public Boolean isProfileImageAgree;

        //����� ������ ����
        @JsonProperty("profile")
        public Profile profile;

        //�̸� ���� ���� ����
        @JsonProperty("name_needs_agreement")
        public Boolean isNameAgree;

        //īī������ �̸�
        @JsonProperty("name")
        public String name;

        //�̸��� ���� ���� ����
        @JsonProperty("email_needs_agreement")
        public Boolean isEmailAgree;

        //�̸����� ��ȿ ����
        // true : ��ȿ�� �̸���, false : �̸����� �ٸ� īī�� ������ ���� ����
        @JsonProperty("is_email_valid")
        public Boolean isEmailValid;

        //�̸����� ���� ����
        //true : ������ �̸���, false : �������� ���� �̸���
        @JsonProperty("is_email_verified")
        public Boolean isEmailVerified;

        //īī������ ��ǥ �̸���
        @JsonProperty("email")
        public String email;

        //���ɴ� ���� ���� ����
        @JsonProperty("age_range_needs_agreement")
        public Boolean isAgeAgree;

        //���ɴ�
        //���� https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
        @JsonProperty("age_range")
        public String ageRange;

        //��� ���� ���� ���� ����
        @JsonProperty("birthyear_needs_agreement")
        public Boolean isBirthYearAgree;

        //��� ���� (YYYY ����)
        @JsonProperty("birthyear")
        public String birthYear;

        //���� ���� ���� ����
        @JsonProperty("birthday_needs_agreement")
        public Boolean isBirthDayAgree;

        //���� (MMDD ����)
        @JsonProperty("birthday")
        public String birthDay;

        //���� Ÿ��
        // SOLAR(���) Ȥ�� LUNAR(����)
        @JsonProperty("birthday_type")
        public String birthDayType;

        //���� ���� ���� ����
        @JsonProperty("gender_needs_agreement")
        public Boolean isGenderAgree;

        //����
        @JsonProperty("gender")
        public String gender;

        //��ȭ��ȣ ���� ���� ����
        @JsonProperty("phone_number_needs_agreement")
        public Boolean isPhoneNumberAgree;

        //��ȭ��ȣ
        //���� ��ȣ�� ��� +82 00-0000-0000 ����
        @JsonProperty("phone_number")
        public String phoneNumber;

        //CI ���� ����
        @JsonProperty("ci_needs_agreement")
        public Boolean isCIAgree;

        //CI, ���� ����
        @JsonProperty("ci")
        public String ci;

        //CI �߱� �ð�, UTC
        @JsonProperty("ci_authenticated_at")
        public Date ciCreatedAt;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {

            //�г���
            @JsonProperty("nickname")
            public String nickName;

            //������ �̸����� �̹��� URL
            @JsonProperty("thumbnail_image_url")
            public String thumbnailImageUrl;

            //������ ���� URL
            @JsonProperty("profile_image_url")
            public String profileImageUrl;

            //������ ���� URL �⺻ ���������� ����
            //true : �⺻ ������, false : ����� ���
            @JsonProperty("is_default_image")
            public String isDefaultImage;

            //�г����� �⺻ �г������� ����
            //true : �⺻ �г���, false : ����� ���
            @JsonProperty("is_default_nickname")
            public Boolean isDefaultNickName;

        }
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Partner {
        //���� ID
        @JsonProperty("uuid")
        public String uuid;
    }


    // �г��� ��ȯ �޼���
    public String getNickname() {
        if (kakaoAccount != null && kakaoAccount.getProfile() != null) {
            return kakaoAccount.getProfile().getNickName();
        }
        return null; // �г��� ������ ���� ��� null ��ȯ
    }
}