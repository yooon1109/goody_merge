package com.honeybee.goody.Chatting;

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
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<Map<Integer,ChatMessage>> allMessages(String roomId) throws Exception {
        return chatService.allChatMessage(roomId);
    }

}