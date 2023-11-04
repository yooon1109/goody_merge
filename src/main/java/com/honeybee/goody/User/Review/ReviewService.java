package com.honeybee.goody.User.Review;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.honeybee.goody.User.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final Firestore firestore;
    private final UserService userService;

    public String saveReviewKeywords(ReviewReceiveDTO review,String receiveId) throws Exception{
        DocumentReference docRef = firestore.collection("Users").document(receiveId);//유저 문서
        CollectionReference subCollectionRef = docRef.collection("Review");//리뷰 서브 컬렉션
        review.setReviewerId(userService.loginUserDocumentId());
        ApiFuture<DocumentReference> result = subCollectionRef.add(review);
        String reviewId = result.get().getId();
        //키워드 카운트
        Map<String,Long> keywords = (Map<String, Long>) docRef.get().get().get("keywords");
        if(keywords.isEmpty()){
            for(int i=1;i<=4;i++){
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

    public ReviewReceiveDTO saveReviewRate(String reviewDocumentId,String receiveId,Double rate) throws Exception{
        DocumentReference docRef = firestore.collection("Users").document(receiveId);//유저 문서
        DocumentReference reviewDocRef = docRef.collection("Review").document(reviewDocumentId);//리뷰 서브 컬렉션 문서
        reviewDocRef.update("rating",rate);

        //별점 총점
        Double sumRate = (Double) docRef.get().get().get("sumRate");
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
        System.out.println(totalKey);
        Double avgRate = (totalKey+sumRate)/reviewCnt;
        docRef.update("avgRate",avgRate);
        return reviewDocRef.get().get().toObject(ReviewReceiveDTO.class);
    }
}
