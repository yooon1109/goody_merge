package com.honeybee.goody.MyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class MyPageUpdateDTO {

    private String nickname;
    private String userPhoneNum;
    private String accountBank;
    private String accountNum;
    private String address;
    private MultipartFile profileImg;
}
