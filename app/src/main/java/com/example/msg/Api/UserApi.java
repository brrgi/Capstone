package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserApi {
    public interface MyCallback {
        void onSuccess(UserModel userModel);
        void onFail(int errorCode, Exception e);
    }

    /*
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void getUserById(String id, final MyCallback myCallback) {
        db.collection("User").whereEqualTo("user_id", id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UserModel usermodel = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                usermodel = documentSnapshot.toObject(UserModel.class);
                            }
                            myCallback.onSuccess(usermodel);
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
    동작: 입력으로 들어온 Id값에 해당하는 모델을 콜백 함수의 onSuccess를 통해서 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */

    public static void postUser(final UserModel userModel, final MyCallback myCallback) {

        db.collection("User").add(userModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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
                        "user_address_detail", userModel.user_address_detail,
                        "user_phone", userModel.user_phone,
                        "email", userModel.email,
                        "mileage", userModel.mileage,
                        "ban_count", userModel.ban_count,
                        "user_grade", userModel.user_grade,
                        "latitude", userModel.latitude,
                        "longitude", userModel.longitude,
                        "user_rating", userModel.user_rating,
                        "ratingCount", userModel.ratingCount

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


