package com.honeybee.goody.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Optional;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class MyPageUpdateDTO {

    private Optional<String> nickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Optional<String> userPhoneNum;
    private Optional<String> accountBank;
    private Optional<String> accountNum;
    private Optional<String> address;
}
