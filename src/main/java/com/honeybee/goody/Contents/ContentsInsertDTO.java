package com.honeybee.goody.Contents;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class ContentsInsertDTO {//프론트에서 받아올 데이터들

    //카테고리
    private String category;
    //상품 거래종류
    private String transType;
    //같이해요일 경우(인원수)
    private Integer numOfPeople;
    //같이해요일 경우(추가 인원)
    private List<String> people;
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
    //파일
    private List<MultipartFile> imgPath;

}
