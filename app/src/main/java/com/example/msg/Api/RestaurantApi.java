package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(RestaurantModel restaurantModel);
        void onFail(int errorCode, Exception e);
    }


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
        db.collection("Restaurant").document(restaurantModel.res_id).
                update(
                        "res_name", restaurantModel.res_name,
                        "res_address", restaurantModel.res_address,
                        "res_address_detail", restaurantModel.res_address_detail,
                        "res_imageURL", restaurantModel.res_imageURL,
                        "res_description", restaurantModel.res_description,
                        "pickup_start_time", restaurantModel.pickup_start_time,
                        "pickup_end_time", restaurantModel.pickup_end_time,
                        "res_phone", restaurantModel.res_phone,
                        "res_email", restaurantModel.res_email
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