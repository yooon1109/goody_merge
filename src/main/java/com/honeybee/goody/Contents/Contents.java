package com.honeybee.goody.Contents;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Contents {//게시글 컨텐츠 등록할 데이터들
    //게시 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;
    //작성자
    private String writerId;
    //카테고리
    private String category;
    //상품 거래종류(같이해요,판매해요,구해요)
    private String transType;
    //상품 등급
    private String grade;
    //게시글 제목
    private String title;
    //상품가격
    private int price;
    //나눔인지 아닌지
    private boolean free;
    //상품 설명
    private String explain;
    //파일 경로
    private List<String> imgPath;
    //
    private String thumbnailImg;
    //품절여부
    private boolean sold;

}
