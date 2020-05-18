package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SaleApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(SaleModel saleModel);
        void onFail(int errorCode, Exception e);
    }


    public static void postSale(final SaleModel saleModel, final MyCallback myCallback) {
        db.collection("Sales").add(saleModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("Restaurant").document(documentReference.getId())
                                .update("sale_id", documentReference.getId());
                        saleModel.sale_id = documentReference.getId();
                        myCallback.onSuccess(saleModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: Model과 콜백함수.
    출력: 없음.
    동작: Model을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 id를 가진 상태)
     */

    public static void getSaleById(String id, final MyCallback myCallback) {
        db.collection("Sales").document(id).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        SaleModel temp = document.toObject(SaleModel.class);
                                        myCallback.onSuccess(temp);
                                    } else {
                                        myCallback.onFail(2, null);
                                        //document no search;
                                    }
                                } else {
                                    myCallback.onFail(1, null);
                                    //exception of firestore
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
    입력: ID
    출력: ID에 대응되는 모델.
    동작: ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델을 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다.
     */
}

