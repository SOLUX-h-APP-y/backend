package com.happy.biling.dto.post;

import lombok.Getter;

@Getter
public class PostWriteRequestDto {
    private Long writerId; //��ū ������ ��ū���� user id �̾ƾ� ���� ������
    private String type; //share borrow
    private String title;
    private Integer price;
    //���⿡ �������� �߰��ؾ���
    private String content;
    private String distance;
    private String category;
    private Integer period; //����Ⱓ
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
}
