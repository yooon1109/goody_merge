package com.honeybee.goody.Contents;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class ContentsDetailDTO {

    //글쓴이 유저문서아이디
    private String writerDocumentId;
    //글쓴이 아이디
    private String writerId;
    //글쓴이 닉네임
    private String nickname;
    //글쓴이 등급
    private String writerGrade;
    //내가 작성한 글인지
    private boolean myContents;
    //내가 좋아요 눌렀는지
    private boolean like;
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
    private Integer price;
    //나눔인지 아닌지
    private boolean free;
    //상품 설명
    private String explain;
    //파일
    private List<String> imgPath;
    //해시태그
    private List<String> hashTags;



}
