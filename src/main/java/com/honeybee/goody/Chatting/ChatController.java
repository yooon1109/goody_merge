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
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<List<ChatMessage>> allMessages(@RequestParam String roomId) throws Exception {
        return ResponseEntity.ok(chatService.allChatMessage(roomId));
    }

    @Operation(summary = "채팅방 판매 물품", description = "")
    @GetMapping("/itemInfo")
    public ResponseEntity<PreviewDTO> itemsInfo(@RequestParam String contentId) throws Exception{
        return ResponseEntity.ok(chatService.getitemInfo(contentId));
    }

    @Operation(summary = "(구매자일 경우)채팅방 My(구매할사용자) 주소,입금할 계좌번호 Seller(게시글올린사용자) 출력")
    @GetMapping("/address-Account")
    public ResponseEntity<Map<String,Object>> address(@RequestParam String roomId)
        throws Exception {
        return ResponseEntity.ok(chatService.getaddress(roomId));
    }

    @Operation(summary = "(구매자일 경우)내 주소 수정")
    @PostMapping("/updateAddress")
    public ResponseEntity<Map<String,Object>> updateAddress(@RequestParam String address) throws Exception{
        return ResponseEntity.ok(chatService.updateAddress(address));
    }

    @Operation(summary = "(판매자일 경우)내 계좌번호 수정")
    @PostMapping("/updateAccount")
    public ResponseEntity<Map<String,Object>> updateAccountNum(@RequestParam String account) throws Exception{
        return ResponseEntity.ok(chatService.updateAccount(account));
    }

    @Operation(summary = "(판매자일 경우)채팅방 구매자들의 주소")
    @GetMapping("/buyerAddress")
    public ResponseEntity<Map<String,Object>> buyerAddress(@RequestParam String roomId)
        throws Exception {
        return ResponseEntity.ok(chatService.buyerAddress(roomId));
    }
}