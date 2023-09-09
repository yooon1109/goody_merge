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

public class CollectionListDTO {

    // 썸네일 이미지
    private String thumbnailPath;


    // 컬렉션 아이디
    private String collectionId;
}
