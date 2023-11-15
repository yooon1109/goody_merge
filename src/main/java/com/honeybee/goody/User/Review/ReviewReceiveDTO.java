package com.honeybee.goody.User.Review;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class ReviewReceiveDTO {

    @Schema(description = "리뷰를 받는사람의 아이디")
    private String reviewerId;

    @Schema(description = "선택된 키워드들")
    private List<String> reviewKeywords;

    @Schema(description = "별점")
    private double rating;

    @Schema(description = "판매물품")
    private String contentsId;

    @Schema(description = "게시글 도큐먼트 아이디")
    private String documentId;
}
