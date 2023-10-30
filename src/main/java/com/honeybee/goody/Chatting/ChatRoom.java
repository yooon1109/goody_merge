package com.honeybee.goody.Chatting;

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
    private String roomId;
    private String roomName;
    private List<String> enterUsers;
}
