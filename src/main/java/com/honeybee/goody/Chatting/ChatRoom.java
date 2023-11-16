package com.honeybee.goody.Chatting;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class ChatRoom {
    @Schema(description = "방아이디",example = "mjc123wejklan")
    private String roomId;
    @Schema(description = "방이름",example = "mjc123")
    private String roomName;
    @Schema(description = "방에 입장한 사람들")
    private List<String> enterUsers;
    @Schema(description = "xxx값 안보내줘도됨xxx")
    private int messageCnt;
    @Schema(description = "컨텐츠문서아이디")
    private String contentsId;
    @Schema(description = "판매자 아이디 ")
    private String sellerId;
    @Schema(description = "구매자 아이디(추후 단체채팅을 위해 배열로)")
    private List<String> buyerId;
    @Schema(description = "")
    private String roomImg;
    private Date lastSend;
}
