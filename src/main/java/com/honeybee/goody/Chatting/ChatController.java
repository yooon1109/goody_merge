package com.honeybee.goody.Chatting;

import com.honeybee.goody.Contents.PreviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
@RestController
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) throws Exception {
        if (ChatMessage.MessageType.JOIN.equals(message.getType()))
        { message.setMessage(message.getSender() + "님이 입장하셨습니다.");}
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatService.saveChatMessage(message);
    }

    @Operation(summary = "채팅방 이전 메시지", description = "")
    @GetMapping("/messages")
    public ResponseEntity<List<Map<Integer,ChatMessage>>> allMessages(@RequestParam String roomId) throws Exception {
        return ResponseEntity.ok(chatService.allChatMessage(roomId));
    }

    @Operation(summary = "채팅방 판매 물품", description = "")
    @GetMapping("/itemInfo")
    public ResponseEntity<PreviewDTO> itemsInfo(@RequestParam String contentId) throws Exception{
        return ResponseEntity.ok(chatService.getitemInfo(contentId));
    }
}