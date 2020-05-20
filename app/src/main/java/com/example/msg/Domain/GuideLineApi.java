package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.GuideLineModel;
import com.example.msg.DatabaseModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GuideLineApi {

    public interface MyCallback {
        void onSuccess(GuideLineModel guideLineModel);

        void onFail(int errorCode, Exception e);
    }
    /*
    에러코드
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static void getUserByName(String name, final MyCallback myCallback) {
        db.collection("GuideLine").whereEqualTo("name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        GuideLineModel guideLineModel = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                guideLineModel = documentSnapshot.toObject(GuideLineModel.class);
                            }
                            myCallback.onSuccess(guideLineModel);
                        } else {
                            myCallback.onFail(1, null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
     /*
    입력: 모델의 Id값과 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 이름값에 해당하는 모델을 콜백 함수의 onSuccess를 통해서 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */

    public static ArrayList<String> getSmallCategoryList(String bigCategory) {
        ArrayList<String> smallCategories = new ArrayList<String>();
        return smallCategories;
    }
    /*
    입력: 대분류의 명칭
    출력: 대분류에 해당하는 소분류 리스트
    동작: 대분류에 해당하는 소분류의 스트링 리스트를 반환합니다.
     */

}
