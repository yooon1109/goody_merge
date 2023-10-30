package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.honeybee.goody.User.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final Firestore firestore;

    public ChatRoom roomCreate(String roomName, List<String> enterUsers) throws Exception{
        CollectionReference collectionRef = firestore.collection("Chats");//컬렉션참조
        DocumentReference docRef = collectionRef.document(roomName);//문서아이디지정
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setEnterUsers(enterUsers);
        docRef.set(chatRoom);

        for(int i=0;i<enterUsers.size();i++){
            QuerySnapshot querySnapshot = firestore.collection("User").whereEqualTo("userId", "mjc123").get().get();;//컬렉션참조
        }
        return chatRoom;
    }
}
