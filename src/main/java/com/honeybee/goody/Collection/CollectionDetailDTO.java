package com.honeybee.goody.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionDetailDTO {
    // 컬렉션 아이디
    private String documentId;

    //컬렉션 제목
    private String title;

    //컬렉션 내용
    private String explain;

    //컬렉션 작성 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;

    // 컬렉션 사진 리스트
    private List<String> filePath;

    private String userId;

    private Integer likeCount;

    private boolean isMyCollection;

    private boolean isLiked;

    //해쉬태크
    private List<String> hashTags;

    private String profileImg;


}
