package com.honeybee.goody.Contents;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "카테고리", example = "ENT,MOV,...")
    private String category;
    //상품 거래종류
    @Schema(description = "거래종류", example = "함께해요,판매해요,...")
    private String transType;
    //같이해요일 경우(인원수)
    @Schema(description = "인원수 같이해요일 경우에만 데이터 전송", example = "3")
    private Integer numOfPeople;
    //같이해요일 경우(추가 인원)
    @Schema(description = "같이해요일 경우에만 데이터 전송 : 나눌 품목(멤버1,2,3...)", example = "")
    private List<String> people;
    //상품 등급
    @Schema(description = "상품등급", example = "A,B(only 대문자)")
    private String grade;
    //게시글 제목
    @Schema(description = "제목", example = "뉴진스포카 팔아요")
    private String title;
    //상품가격
    @Schema(description = "가격", example = "12000")
    private Integer price;
    //나눔인지 아닌지
    @Schema(description = "나눔여부", example = "true,false")
    private boolean free;
    //상품 설명
    @Schema(description = "상품 설명", example = "포토카드 팔아요 어쩌구 저쩌구")
    private String explain;
    //파일
    @Schema(description = "파일", example = "")
    private List<MultipartFile> imgPath;
    @Schema(description = "해시태그", example = "")
    private List<String> hashTags;

}