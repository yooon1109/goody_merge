package com.honeybee.goody.Chatting;

import com.honeybee.goody.Collection.CollectionDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @Operation(summary = "채팅방 목록", description = "")
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoom>> getChatRoomList() throws Exception {
        return ResponseEntity.ok(chatRoomService.chatRoomList());
    }

    @Operation(summary = "채팅방 생성", description = "")
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) throws Exception {

        return ResponseEntity.ok(chatRoomService.roomCreate(chatRoom));
    }

//    @Operation(summary = "채팅방 입장", description = "")
//    @PostMapping("/enter")
//    public ResponseEntity<ChatRoom> enterChatRoom(String roomName) throws Exception {
//        return null;
//    }

    @Operation(summary = "채팅방 삭제", description = "채팅방 삭제하기")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseEntity<String>> chatRoomDelete(@RequestParam String roomId, @RequestParam List<String> enterUsers)
            throws Exception{
        return ResponseEntity.ok(chatRoomService.deleteChatRoom(roomId, enterUsers));
    }
}
