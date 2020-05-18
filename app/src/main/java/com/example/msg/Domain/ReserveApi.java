package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReserveApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(ReserveModel reserveModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<ReserveModel> reserveModels);
        void onFail(int errorCode, Exception e);
    }

    public static void postResrvation2(final ReserveModel reserveModel, final MyCallback myCallback) {
        db.collection("Reservations").add(reserveModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("Reservations").document(documentReference.getId())
                                .update("reservation_id", documentReference.getId());
                        reserveModel.reservation_id = documentReference.getId();
                        myCallback.onSuccess(reserveModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    //TODO 아래의 postReservation 함수를 이것으로 대체할것.


    public static void postReservation(ReserveModel reserveModel) {
        db.collection("Reservations").add(reserveModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("Reservations").document(documentReference.getId())
                                .update("reservation_id", documentReference.getId());
                    }
                });
    }//예약 추가



    public static void getReservationByUserId(String id, final MyCallback myCallback) {
        db.collection("Reservations").document(id).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ReserveModel temp = document.toObject(ReserveModel.class);
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
    입력: id와 콜백함수.
    출력: iD에 대응되는 모델.
    동작: iD를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델을 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다. 실패 시 onFail을 호출합니다.
     */
}
