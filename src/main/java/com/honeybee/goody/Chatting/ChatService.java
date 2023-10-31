package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Firestore firestore;

    public String saveChatMessage(ChatMessage message) throws Exception{
        DocumentReference docRef = firestore.collection("Chats").document(message.getRoomId());//문서
        Long messageCnt = (Long) docRef.get().get().get("messageCnt");
        CollectionReference subCollectionRef = docRef.collection("Messages");
        subCollectionRef.document(String.valueOf(messageCnt+1)).set(message);
        docRef.update("messageCnt",messageCnt+1);
        return subCollectionRef.getId();
    }

    public List<Map<Integer,ChatMessage>> allChatMessage(String roomId) throws Exception{
        DocumentReference docRef = firestore.collection("Chats").document(roomId);//문서
        Long messageCnt = (Long) docRef.get().get().get("messageCnt");
        CollectionReference subCollectionRef = docRef.collection("Messages");
        List<Map<Integer,ChatMessage>> chatMessageList = new ArrayList<>();
        for(int i=1;i<=messageCnt;i++){
            DocumentSnapshot documentSnapshot = subCollectionRef.document(String.valueOf(i)).get().get();
            ChatMessage chatMessage = documentSnapshot.toObject(ChatMessage.class);
            Map<Integer,ChatMessage> chatMessageMap = new HashMap<>();
            chatMessageMap.put(i,chatMessage);
            chatMessageList.add(chatMessageMap);
        }
        return chatMessageList;
    }
}
