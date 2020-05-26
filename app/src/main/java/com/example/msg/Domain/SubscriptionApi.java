package com.example.msg.Domain;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SubscriptionModel;
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

public class SubscriptionApi {
    public interface MyCallback {
        void onSuccess(SubscriptionModel subscriptionModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList);
        void onFail(int errorCode, Exception e);
    }

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void postSubscription(final SubscriptionModel subscriptionModel, final MyCallback myCallback) {

        db.collection("Subscription").add(subscriptionModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("Subscription").document(documentReference.getId())
                                .update("subs_id", documentReference.getId());
                        subscriptionModel.subs_id = documentReference.getId();
                        myCallback.onSuccess(subscriptionModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 구독DB모델과 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 DB 모델을 데이터베이스에 등록합니다. 성공시 콜백함수의 onSuccess가, 실패시 onFail이 동작합니다.
     */


    public static void getSubscriptionById(String subscriptionId, final  MyCallback myCallback) {
        db.collection("Subscription").document(subscriptionId).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        SubscriptionModel temp = document.toObject(SubscriptionModel.class);
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
    입력: 구독 모델의 Id값과 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 Id값에 해당하는 구독 모델을 콜백 함수의 onSuccess를 통해서 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */


    public static void getSubscriptionListByUserId(final String userId, final MyListCallback myCallback) {
        db.collection("Subscription").
                whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SubscriptionModel subscriptionModel = null;
                        ArrayList<SubscriptionModel> subscriptionModelArrayList = new ArrayList<SubscriptionModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscriptionModel = document.toObject(SubscriptionModel.class);
                                subscriptionModelArrayList.add(subscriptionModel);
                            }
                            myCallback.onSuccess(subscriptionModelArrayList);

                        } else {
                            myCallback.onFail(1, null);
                            //태스크 실패.
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e); //실패.
            }
        });
    }
    /*
    입력: 유저 id와 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 유저 아이디를 가지는 구독 모델을 콜백 함수의 onSuccess를 통해서 리스트 형태로 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */

    public static void getSubscriptionListByResId(final String resId, final MyListCallback myCallback) {
        db.collection("Subscription").
                whereEqualTo("res_id", resId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SubscriptionModel subscriptionModel = null;
                        ArrayList<SubscriptionModel> subscriptionModelArrayList = new ArrayList<SubscriptionModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscriptionModel = document.toObject(SubscriptionModel.class);
                                subscriptionModelArrayList.add(subscriptionModel);
                            }
                            myCallback.onSuccess(subscriptionModelArrayList);

                        } else {
                            myCallback.onFail(1, null);
                            //태스크 실패.
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e); //실패.
            }
        });
    }
    /*
    입력: 레스토랑 id와 콜백 함수.
    출력: 없음.
    동작: 입력으로 들어온 식당 아이디값을 가지는 구독 모델을 콜백 함수의 onSuccess를 통해서 리스트 형태로 돌려줍니다. 실패시 onFail함수의 로직이 동작합니다.
     */

    //TODO: 구독 삭제 기능 추가해야함.
    public static void deleteSubscriptionBySubsId(final String subsId, final MyCallback myCallback) {
        db.collection("Subsciption").document(subsId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SubscriptionModel subscriptionModel = null;
                        if(task.isSuccessful()) {
                            myCallback.onSuccess(subscriptionModel);
                        }
                        else {
                            myCallback.onFail(1,null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(1,null);

            }
        });
    }

}
