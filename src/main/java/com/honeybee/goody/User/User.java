package com.honeybee.goody.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


@Getter
@Setter
public class User {

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
    private int collectionCnt;
    private String grade;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date joinDate;
    private List<String> contentsLikes;
    private List<String> chatRooms;
    private Map<String,Integer> keywords;
    private int reviewCnt;
    private Long sumRate;
    private double avgRate;
    private List<String> collectionLikes;
    private List<String> userCollectionId;
    private List<String> userContentsId;

}
