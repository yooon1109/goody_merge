package com.honeybee.goody.Chatting;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.honeybee.goody.Contents.Contents;
import com.honeybee.goody.Contents.PreviewDTO;
import com.honeybee.goody.User.UserService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Firestore firestore;
    private final UserService userService;

    public String saveChatMessage(ChatMessage message) throws Exception{
        DocumentReference docRef = firestore.collection("Chats").document(message.getRoomId());//문서
        Long messageCnt = (Long) docRef.get().get().get("messageCnt");
        CollectionReference subCollectionRef = docRef.collection("Messages");
        subCollectionRef.document(String.valueOf(messageCnt+1)).set(message);
        docRef.update("messageCnt",messageCnt+1);
        docRef.update("lastSend",message.getTime());
        return subCollectionRef.getId();
    }

    public List<ChatMessage> allChatMessage(String roomId) throws Exception{
        DocumentReference docRef = firestore.collection("Chats").document(roomId);//문서
        Long messageCnt = (Long) docRef.get().get().get("messageCnt");
        CollectionReference subCollectionRef = docRef.collection("Messages");
        List<ChatMessage> chatMessageList = new ArrayList<>();

        for(int i=1;i<=messageCnt;i++){
            DocumentSnapshot documentSnapshot = subCollectionRef.document(String.valueOf(i)).get().get();
            ChatMessage chatMessage = documentSnapshot.toObject(ChatMessage.class);
            if(chatMessage!=null){
                chatMessageList.add(chatMessage);
            }
        }
        return chatMessageList;
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

    public Map<String,Object> updateAddress(String address) throws Exception {
        Map<String,Object> response = new HashMap<>();
        String loginUserId = userService.loginUserDocumentId();
        DocumentReference docRef = firestore.collection("Users").document(loginUserId);
        docRef.update("address",address);
        String updated = docRef.get().get().getString("address");
        response.put("MyAddress",updated);
        return response;
    }

    public Map<String,Object> updateAccount(String accountNum) throws Exception {
        Map<String,Object> response = new HashMap<>();
        String loginUserId = userService.loginUserDocumentId();
        DocumentReference docRef = firestore.collection("Users").document(loginUserId);
        docRef.update("accountNum",accountNum);
        String updated = docRef.get().get().getString("accountNum");
        response.put("accountNum",updated);
        return response;
    }

    public Map<String,Object> buyerAddress(String roomId) throws Exception {
        Map<String,Object> response = new HashMap<>();
        DocumentReference docRef = firestore.collection("Chats").document(roomId);//문서

        //판매자 정보
        String sellerId = docRef.get().get().getString("sellerId");
        QuerySnapshot querySnapshot = firestore.collection("Users").whereEqualTo("userId",sellerId).get().get();
        QueryDocumentSnapshot seller = querySnapshot.getDocuments().get(0);
        String accountNum = seller.getString("accountNum");
        String accountBank = seller.getString("accountBank");

        List<String> enterUsers = (List<String>) docRef.get().get().get("enterUsers");//채팅방 입장 유저들

        //현재 로그인 유저
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginUser = userDetails.getUsername();//현재 로그인한 유저의 아이디
        if(loginUser.equals(sellerId)){//판매자일 경우
            response.put("role","seller");
            enterUsers.remove(sellerId);
            Map<String,Object> buyerAddress = new LinkedHashMap<>();
            for(String buyer:enterUsers){
                QuerySnapshot buyerquerySnapshot = firestore.collection("Users").whereEqualTo("userId",buyer).get().get();
                QueryDocumentSnapshot buyUser = buyerquerySnapshot.getDocuments().get(0);
                String address = (String) buyUser.get("address");
                if(address!=null) buyerAddress.put(buyer,address);
                else buyerAddress.put(buyer,"주소가 입력되지 않았습니다.");
            }
            response.put("address",buyerAddress);

            response.put("MyAccountNum",accountNum);
            if(accountBank!=null) response.put("MyAccountBank",accountBank);
            else response.put("MyAccountBank","계좌번호를 입력해주세요");
        }
        else if(enterUsers.contains(loginUser)){
            response.put("role","buyer");
            response.put("sellerAccountNum",accountNum);
            if(accountBank!=null) response.put("sellerAccountBank",accountBank);
            else response.put("sellerAccountBank","계좌번호가 입력되지 않았습니다.");
            String loginUserId = userService.loginUserDocumentId();
            String loginUserAddress = firestore.collection("Users").document(loginUserId).get().get().getString("address");
            if(loginUserAddress!=null) response.put("MyAddress",loginUserAddress);
            else response.put("MyAddress","주소를 입력해주세요");
        }
        return response;
    }
}
