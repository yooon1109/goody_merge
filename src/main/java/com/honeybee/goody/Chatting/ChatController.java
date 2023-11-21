package com.honeybee.goody.Chatting;

import com.honeybee.goody.Contents.PreviewDTO;
import io.swagger.v3.oas.annotations.Operation;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        chatService.saveChatMessage(message);
        if(ChatMessage.MessageType.IMG.equals(message.getType())){
            String encodedURL = URLEncoder.encode(message.getMessage(), "UTF-8");
            message.setMessage("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
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

    @Operation(summary = "채팅방사진")
    @PostMapping(value = "/sendImg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> sendImage(@RequestPart MultipartFile multipartFile,@RequestParam String roomId) throws Exception{
        String filePath = chatService.saveChatImg(multipartFile,roomId);
        Map<String,Object> response = new HashMap<>();
        response.put("data",filePath);
        return ResponseEntity.ok(response);
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

    @Operation(summary = "채팅방 판매자/구매자에 따른 주소,계좌")
    @GetMapping("/addressInfo")
    public ResponseEntity<Map<String,Object>> buyerAddress(@RequestParam String roomId)
        throws Exception {
        return ResponseEntity.ok(chatService.buyerAddress(roomId));
    }
}