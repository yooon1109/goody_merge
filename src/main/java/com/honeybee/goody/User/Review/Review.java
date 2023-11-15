package com.honeybee.goody.User.Review;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Review {

    @Schema(description = "별점")
    private double rating;

    @Schema(description = "받은리뷰개수")
    private int reviewCnt;

    @Schema(description = "받은리뷰키워드")
    private Map<String,Integer> keywordsCnt;

    @Schema(description = "전체평점")
    private double totalRate;

    @Schema(description = "게시글 도큐먼트 아이디")
    private String documentId;
}
