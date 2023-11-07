package com.honeybee.goody.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
@Getter
@Setter
public class MyPage {

    private String userId;
    private String userPw;
    private String name;
    private String nickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth;
    private String userPhoneNum;
    private String accountBank;
    private String accountNum;
    private String address;
    private String grade;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date joinDate;

}
