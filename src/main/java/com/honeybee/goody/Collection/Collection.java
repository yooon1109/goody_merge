package com.honeybee.goody.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Collection {
    //컬렉션 제목
    private String title;

    //컬렉션 내용
    private String content;

    //컬렉션 작성 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    //컬렉션 사진
    private List<String> filePath;
}