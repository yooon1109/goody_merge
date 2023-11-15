package com.honeybee.goody.MyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class MyPageReviewDTO {

    private String nickname;

    private String grade;

    private Map<String,Integer> keywords;

}
