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
public class MyPageHomeDTO {

    private String nickname;

    private String grade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date joinDate;

    private String accountNum;

    private long daysSinceJoin;

    public void setDaysSinceJoin(long daysSinceJoin) {
        this.daysSinceJoin = daysSinceJoin;
    }
}
