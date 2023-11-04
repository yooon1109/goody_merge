package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.honeybee.goody.User.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final Firestore firestore;
    private final UserService userService;
    public ChatRoom roomCreate(ChatRoom chatRoom) throws Exception{
        CollectionReference collectionRef = firestore.collection("Chats");//컬렉션참조
        DocumentReference docRef = collectionRef.document(chatRoom.getRoomId());//문서아이디지정

        chatRoom.setMessageCnt(0);
        docRef.set(chatRoom);

        //참가한 사용자들의 채팅방목록 업데이트
        for(int i=0;i<chatRoom.getEnterUsers().size();i++){
            CollectionReference colRef = firestore.collection("Users");//컬렉션참조
            DocumentSnapshot documentSnapshot = colRef.whereEqualTo("userId", chatRoom.getEnterUsers().get(i))
                                                    .get().get().getDocuments().get(0);
            String documentId = documentSnapshot.getId();//문서 아이디
            colRef.document(documentId).update("chatRooms", FieldValue.arrayUnion(chatRoom.getRoomId()));
        }
        return chatRoom;
    }

    public List<ChatRoom> chatRoomList() throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        Object chatRoom = userDocRef.get().get().get("chatRooms");
        List<String> chatRooms = (List<String>) chatRoom;
        List<ChatRoom> chatRoomList = new ArrayList<>();
        for(String chatRoomId : chatRooms){
            DocumentSnapshot chatDocSnap = firestore.collection("Chats").document(chatRoomId).get().get();
            ChatRoom room = chatDocSnap.toObject(ChatRoom.class);
            chatRoomList.add(room);
        }
        return chatRoomList;
    }
}
