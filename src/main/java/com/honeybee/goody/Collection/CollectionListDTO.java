package com.honeybee.goody.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
