package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.SubscriptionModel;
import com.example.msg.DatabaseModel.UserModel;
import com.firebase.ui.auth.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserApi {
    public interface MyCallback {
        void onSuccess(UserModel userModel);
        void onFail(int errorCode, Exception e);
    }

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void getUserById(String id, final MyCallback myCallback) {
        db.collection("User").document(id).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        UserModel temp = document.toObject(UserModel.class);
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
    입력: 모델의 Id값과 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 Id값에 해당하는 모델을 콜백 함수의 onSuccess를 통해서 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */

    public static void postUser(final UserModel userModel, final MyCallback myCallback) {

        db.collection("User").add(userModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("User").document(documentReference.getId())
                                .update("user_id", documentReference.getId());
                        userModel.user_id = documentReference.getId();
                        myCallback.onSuccess(userModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 모델과 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 DB 모델을 데이터베이스에 등록합니다. 성공시 콜백함수의 onSuccess가, 실패시 onFail이 동작합니다.
     */

    public static void updateUser(final UserModel userModel, final MyCallback myCallback) {
        db.collection("User").document(userModel.user_id).
                update(
                        "user_id", userModel.user_id,
                        "user_name", userModel.user_name,
                        "age", userModel.age,
                        "is_male", userModel.is_male,
                        "user_address", userModel.user_address,
                        "user_phone", userModel.user_phone,
                        "email", userModel.email,
                        "mileage", userModel.mileage,
                        "ban_count", userModel.ban_count,
                        "user_grade", userModel.user_grade
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(userModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: Model
    출력: 없음
    동작: Model을 받아서 해당 객체에 대응되는 데이터베이스 자리에 값을 업데이트합니다. 성공할 경우 콜백 함수를 통해서 성공한 객체를
    그대로 반환하며, 실패할 경우는 onFail 콜백함수를 부릅니다.
     */

}


