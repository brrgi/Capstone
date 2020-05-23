package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RestaurantApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(RestaurantModel restaurantModel);
        void onFail(int errorCode, Exception e);
    }
    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */

    public static void postRestaurant(final RestaurantModel restaurantModel, final MyCallback myCallback) {
        db.collection("Restaurant").add(restaurantModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        myCallback.onSuccess(restaurantModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: RestaurantModel과 콜백함수.
    출력: 없음.
    동작: Model을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 id를 가진 상태)
     */

    public static void getUserById(String id, final MyCallback myCallback) {
        db.collection("Restaurant").whereEqualTo("res_id", id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantModel restaurantModel = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                restaurantModel = documentSnapshot.toObject(RestaurantModel.class);
                            }
                            myCallback.onSuccess(restaurantModel);
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
    입력: ID
    출력: ID에 대응되는 모델.
    동작: ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델을 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다.
     */

    public static void updateRestaurant(final RestaurantModel restaurantModel, final MyCallback myCallback) {
        db.collection("ResProducts").document(restaurantModel.res_id).
                update(
                        "res_id", restaurantModel.res_id,
                        "res_name", restaurantModel.res_name,
                        "res_address", restaurantModel.res_address,
                        "res_imageURL", restaurantModel.res_imageURL,
                        "res_description", restaurantModel.res_description,
                        "pickup_start_time", restaurantModel.pickup_start_time,
                        "pickup_end_time", restaurantModel.pickup_end_time
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(restaurantModel);
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