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
public class Collection {
    //컬렉션 제목
    private String title;

    //컬렉션 내용
    private String explain;

    //컬렉션 작성 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;

    //컬렉션 내용 업데이트 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updatedDate;

    // 썸네일 이미지
    private String thumbnailPath;

    //이미지 경로
    private List<String> filePath;

    //컬렉션 작성자 도큐먼트ID
    private String userId;

    //좋아요 개수
    private Integer LikeCount;

    //해쉬태크
    private List<String> hashTags;

    //컬렉션 좋아요한 유저 아이디들
    private List<String> collectionLikesUserId;

}
