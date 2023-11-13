package com.honeybee.goody.User.Review;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.honeybee.goody.User.UserService;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final Firestore firestore;
    private final UserService userService;

    public String saveReviewKeywords(ReviewReceiveDTO review,String receiveId) throws Exception{
        DocumentReference docRef = firestore.collection("Users").whereEqualTo("userId",receiveId).get().get().getDocuments().get(0).getReference();//유저 문서
        CollectionReference subCollectionRef = docRef.collection("Review");//리뷰 서브 컬렉션
        review.setReviewerId(userService.loginUserDocumentId());
        ApiFuture<DocumentReference> result = subCollectionRef.add(review);
        String reviewId = result.get().getId();
        //키워드 카운트
        Map<String,Long> keywords = (Map<String, Long>) docRef.get().get().get("keywords");
        if(keywords==null||keywords.size()<=12){
            keywords = new HashMap<>();
            for(int i=1;i<=6;i++){
                keywords.put("good"+i, 0L);
                keywords.put("bad"+i,0L);
            }
        }
        for(String keyword: review.getReviewKeywords()){
            if (keywords.containsKey(keyword)) {
                // 해당 키에 해당하는 값을 업데이트
                long updatedValue = keywords.get(keyword) + 1;
                keywords.put(keyword, updatedValue);
            }
        }
        docRef.update("keywords",keywords);
        //받은리뷰수 +1
        Long reviewCnt = (Long) docRef.get().get().get("reviewCnt");
        if(reviewCnt!=null){
            docRef.update("reviewCnt",reviewCnt+1);
        }else{
            docRef.update("reviewCnt",1);
        }

        return reviewId;
    }

    public Map<String,Object> saveReviewRate(String reviewDocumentId,String receiveId,Long rate) throws Exception{
        DocumentReference docRef = firestore.collection("Users").whereEqualTo("userId",receiveId).get().get().getDocuments().get(0).getReference();//유저 문서
        DocumentReference reviewDocRef = docRef.collection("Review").document(reviewDocumentId);//리뷰 서브 컬렉션 문서
        reviewDocRef.update("rating",rate);

        //별점 총점
        Long sumRate = (Long) docRef.get().get().get("sumRate");
        if(sumRate!=null){
            sumRate+=rate;
        }else{
            sumRate = rate;
        }
        docRef.update("sumRate",sumRate);

        //등급계산
        Long reviewCnt = (Long) docRef.get().get().get("reviewCnt");
        //키워드 점수
        Map<String,Long> keywords = (Map<String, Long>) docRef.get().get().get("keywords");
        Long totalKey = 0L;
        for (String key : keywords.keySet()) {
            Long keyCnt = keywords.get(key);
            if(key.startsWith("good")){
                totalKey += (keyCnt*(+25));
            } else if (key.startsWith("bad")) {
                totalKey += (keyCnt*(-25));
            }
        }
        docRef.update("keywordScore",totalKey);
        Long totalScore = totalKey + sumRate;
        Map<String,Object> grade = new HashMap<>();
        if(totalScore>5000 && reviewCnt>40){
            docRef.update("grade","여왕벌");
            grade.put("grade","여왕벌");
        }
        else if(totalKey>2500 && reviewCnt>20){
            docRef.update("grade","꿀벌");
            grade.put("grade","꿀벌");
        }
        else if(totalKey>1000 && reviewCnt>7){
            docRef.update("grade","애기꿀벌");
            grade.put("grade","애기꿀벌");
        }
        else if(totalKey>0 && reviewCnt>0){
            docRef.update("grade","애벌레");
            grade.put("grade","애벌레");
        }
        else{
            docRef.update("grade","장수말벌");
            grade.put("grade","장수말벌");
        }
        return grade;
    }

}
