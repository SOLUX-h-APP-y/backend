package com.happy.biling.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostWriteRequestDto {
    private String type; //share borrow
    private String title;
    private Integer price;
    //여기에 사진까지 추가해야함
    private String content;
    private String distance;
    private String category;
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
}
