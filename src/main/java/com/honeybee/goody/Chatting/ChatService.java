package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.honeybee.goody.Contents.Contents;
import com.honeybee.goody.Contents.PreviewDTO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public Map<Integer,ChatMessage> allChatMessage(String roomId) throws Exception{
        DocumentReference docRef = firestore.collection("Chats").document(roomId);//문서
        Long messageCnt = (Long) docRef.get().get().get("messageCnt");
        CollectionReference subCollectionRef = docRef.collection("Messages");
        Map<Integer,ChatMessage> chatMessageMap = new HashMap<>();
//        List<Map<Integer,ChatMessage>> chatMessageList = new ArrayList<>();
        for(int i=1;i<=messageCnt;i++){
            DocumentSnapshot documentSnapshot = subCollectionRef.document(String.valueOf(i)).get().get();
            ChatMessage chatMessage = documentSnapshot.toObject(ChatMessage.class);
            chatMessageMap.put(i,chatMessage);
//            chatMessageList.add(chatMessageMap);
        }
        return chatMessageMap;
    }

    public PreviewDTO getitemInfo(String contentsId) throws Exception {
        DocumentReference docRef = firestore.collection("Contents").document(contentsId);//문서
        Contents contents = docRef.get().get().toObject(Contents.class);
        ModelMapper modelMapper = new ModelMapper();
        PreviewDTO previewDTO = modelMapper.map(contents, PreviewDTO.class);
        previewDTO.setDocumentId(docRef.getId()); // 문서의 ID를 설정
        try {
            String encodedURL = URLEncoder.encode(previewDTO.getThumbnailImg(), "UTF-8");
            previewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return previewDTO;
    }

}
