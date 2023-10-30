package com.honeybee.goody.Chatting;

import com.honeybee.goody.Collection.CollectionDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @Operation(summary = "채팅방 목록", description = "")
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoom>> getChatRoomList() throws Exception {
        return null;
    }

    @Operation(summary = "채팅방 생성", description = "")
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String roomName,@RequestParam List<String> enterUsers) throws Exception {

        return ResponseEntity.ok(chatRoomService.roomCreate(roomName,enterUsers));
    }

    @Operation(summary = "채팅방 입장", description = "")
    @PostMapping("/enter")
    public ResponseEntity<ChatRoom> enterChatRoom(String roomName) throws Exception {
        return null;
    }
}
