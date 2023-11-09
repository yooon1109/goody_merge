package com.honeybee.goody.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionLikeDTO {
    //좋아요 개수
    private Integer LikeCount;
    //좋아요 한 사용자 아이디
    private List<String> collectionLikesUserId;

}
