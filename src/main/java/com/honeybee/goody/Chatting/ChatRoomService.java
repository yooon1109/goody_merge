package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.honeybee.goody.User.User;
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
    public ChatRoom roomCreate(String roomId, List<String> enterUsers) throws Exception{
        CollectionReference collectionRef = firestore.collection("Chats");//컬렉션참조
        DocumentReference docRef = collectionRef.document(roomId);//문서아이디지정
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setEnterUsers(enterUsers);
        chatRoom.setMessageCnt(0);
        docRef.set(chatRoom);

        //참가한 사용자들의 채팅방목록 업데이트
        for(int i=0;i<enterUsers.size();i++){
            CollectionReference colRef = firestore.collection("Users");//컬렉션참조
            DocumentSnapshot documentSnapshot = colRef.whereEqualTo("userId", enterUsers.get(i))
                                                    .get().get().getDocuments().get(0);
            String documentId = documentSnapshot.getId();//문서 아이디
            colRef.document(documentId).update("ChatRooms", FieldValue.arrayUnion(roomId));
        }
        return chatRoom;
    }

    public List<ChatRoom> chatRoomList() throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        Object chatRoom = userDocRef.get().get().get("ChatRooms");
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
