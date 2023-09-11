package com.honeybee.goody.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PostDTO {

    //카테고리
    private String category;
    //상품 거래종류
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
    //게시글 등록 날짜
    private Date postDate;
    //파일경로
    private List<String> filePath;

}
