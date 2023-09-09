package com.honeybee.goody.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionDTO {
    //컬렉션 제목
    private String title;

    //컬렉션 내용
    private String content;

    // 컬렉션 아이디
    private String collectionId;

    //컬렉션 작성 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date uploadDate;

    // 썸네일 이미지
    private String thumbnailPath;

    // 컬렉션 사진 리스트
    private List<String> imgPath;
}
