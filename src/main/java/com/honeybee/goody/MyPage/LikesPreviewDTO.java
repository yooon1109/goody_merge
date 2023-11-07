package com.honeybee.goody.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class LikesPreviewDTO {

    private String documentId;

    private String title;

    private String transType;

    private Integer price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;

    private String category;

    private String thumbnailImg;

}
