package com.honeybee.goody.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class PreviewDTO {//게시글 미리보기(썸네일)에 필요한 데이터들만
    private String title;

    private String transType;

    private int price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date postDate;

    private String category;

    private String filePath;
}