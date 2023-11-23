package com.honeybee.goody.Chatting;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.honeybee.goody.User.UserService;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final Firestore firestore;
    private final UserService userService;
    public ChatRoom roomCreate(ChatRoom chatRoom) throws Exception{
        CollectionReference collectionRef = firestore.collection("Chats");//컬렉션참조
        DocumentReference docRef = collectionRef.document(chatRoom.getRoomId());//문서아이디지정
        String img = firestore.collection("Contents").document(chatRoom.getContentsId()).get().get().getString("thumbnailImg");
        chatRoom.setRoomImg(img);
        chatRoom.setMessageCnt(0);
        LocalDateTime localDateTime = LocalDateTime.now();
        // LocalDateTime을 Instant로 변환
        java.time.Instant instant = localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant();
        // Instant를 Date로 변환
        Date createdDate = java.util.Date.from(instant);
        chatRoom.setLastSend(createdDate);
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
            String img = room.getRoomImg();
            String encodedURL = URLEncoder.encode(img, "UTF-8");
            room.setRoomImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
            chatRoomList.add(room);
        }
        // lastSend를 기준으로 정렬
        chatRoomList.sort(Comparator.comparing(ChatRoom::getLastSend, Comparator.nullsLast(Comparator.reverseOrder())));

        return chatRoomList;
    }

    //채팅방 삭제
    public ResponseEntity<String> deleteChatRoom(String roomId) throws Exception{
        CollectionReference collectionRef = firestore.collection("Chats");
        DocumentReference doc = collectionRef.document(roomId);
        DocumentSnapshot documentSnapshot = doc.get().get();


        List<String> enterUsers = (List<String>) documentSnapshot.get("enterUsers");

        ApiFuture<WriteResult> deleteApiFuture = doc.delete();
        deleteSubcollection(doc, "Messages");
        // 채팅방 문서 삭제
        doc.delete().get();


        //enterUsers에 있는 유저들의 chatRooms 배열 필드에서 해당 채팅방 삭제
        CollectionReference usersRef = firestore.collection("Users");
        for (String userId : enterUsers) {
            Query query = usersRef.whereEqualTo("userId", userId);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document.exists()) {
                    DocumentReference userDocRef = document.getReference();
                    // chatRooms 필드에서 roomId 삭제
                    userDocRef.update("chatRooms", FieldValue.arrayRemove(roomId)).get();
                }
            }
        }

        return ResponseEntity.ok("채팅방 삭제 성공");
    }

    //하위컬렉션 사진 삭제하는거
    private void deleteSubcollection(DocumentReference docRef, String subcollectionName) throws Exception {
        // 하위 컬렉션의 모든 문서를 얻음
        ApiFuture<QuerySnapshot> future = docRef.collection(subcollectionName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // 각 문서 삭제
        for (DocumentSnapshot document : documents) {
            document.getReference().delete().get();
        }
    }


}
