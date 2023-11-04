package com.honeybee.goody.User.Review;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final Firestore firestore;

    public String saveReviewKeywords(Review review){
        DocumentReference docRef = firestore.collection("Users").document(review.getRecipientUserId());//유저 문서
        CollectionReference subCollectionRef = docRef.collection("Review");//리뷰 서브 컬렉션

        return null;
    }
}
