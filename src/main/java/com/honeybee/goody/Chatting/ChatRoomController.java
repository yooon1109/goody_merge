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
        return ResponseEntity.ok(chatRoomService.chatRoomList());
    }

    @Operation(summary = "채팅방 생성", description = "")
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@Parameter(description = "알아서 규칙적으로 생성해서 보내주기")@RequestParam String roomId,
                                                   @Parameter(description = "채팅방에 참여한 인원들(판매자+구매자)") @RequestParam List<String> enterUsers,
                                                   @Parameter(description = "구매하려는 물품의 아이디") @RequestParam String contentId) throws Exception {

        return ResponseEntity.ok(chatRoomService.roomCreate(roomId,enterUsers,contentId));
    }

//    @Operation(summary = "채팅방 입장", description = "")
//    @PostMapping("/enter")
//    public ResponseEntity<ChatRoom> enterChatRoom(String roomName) throws Exception {
//        return null;
//    }
}
