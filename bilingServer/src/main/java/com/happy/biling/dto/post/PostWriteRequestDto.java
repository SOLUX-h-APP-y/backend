package com.happy.biling.dto.post;

import lombok.Getter;

@Getter
public class PostWriteRequestDto {
    private Long writerId; //토큰 생성시 토큰에서 user id 뽑아쓸 수도 있을듯
    private String type; //share borrow
    private String title;
    private Integer price;
    //여기에 사진까지 추가해야함
    private String content;
    private String distance;
    private String category;
    private Integer period; //공고기간
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
}
