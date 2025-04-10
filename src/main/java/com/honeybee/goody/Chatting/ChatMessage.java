package com.honeybee.goody.Chatting;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    // 메시지 타입 : 입장, 채팅
    enum MessageType {
        TALK,JOIN,IMG
    }

    private MessageType type; //메시지 타입
    private String roomId;// 방 번호
    private String sender;//채팅을 보낸 사람
    private String message;// 메세지
    private Date time; // 채팅 발송 시간
}