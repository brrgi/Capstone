package com.example.msg.Api;

// TODO: 2020-06-11
// 내 아이디랑 똑같으면서 날짜별로
// first  :  어제 날짜인 거
// second :  오늘 날짜인 거
// third  :  이번 달 날짜인 거
// fourth :  총 매출

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StatisticsApi {

    public interface MyCallback {
        void onSuccess(ArrayList<Integer> sum);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList);
        void onFail(int errorCode, Exception e);
    }


    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void getMen(final MyCallback myCallback){
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("User")
                .whereEqualTo("is_male", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UserModel userModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userModel = document.toObject(userModel.class);
                                sum.add(1);

                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public static void getWomen(final MyCallback myCallback){
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("User")
                .whereEqualTo("is_male", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UserModel userModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userModel = document.toObject(userModel.class);
                                sum.add(1);

                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public static void getYesterdayCost(final String id, final int completed ,int year, int month, int day, final MyCallback myCallback) {
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("ResProducts")
                .whereEqualTo("res_id", id)
                .whereEqualTo("completed", completed)
                .whereEqualTo("saleDateYear", year)
                .whereEqualTo("saleDateMonth", month)
                .whereEqualTo("saleDateDay", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                sum.add(restaurantProductModel.cost);

                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }
    /*
    입력: uid와 completed 상태, 어제 년 월 일.
    출력: 없음.
    동작: 어제 번 돈을 구합니다.
     */


    public static void getTodayCost(final String id, final int completed ,int year, int month, int day, final MyCallback myCallback) {
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("ResProducts")
                .whereEqualTo("res_id", id)
                .whereEqualTo("completed", completed)
                .whereEqualTo("saleDateYear", year)
                .whereEqualTo("saleDateMonth", month)
                .whereEqualTo("saleDateDay", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                sum.add(restaurantProductModel.cost);

                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }
    /*
    입력: uid와 completed 상태, 어제 년 월 일.
    출력: 없음.
    동작: 오늘 번 돈을 구합니다.
     */

    public static void getMonthCost(final String id, final int completed ,int year, int month, final MyCallback myCallback) {
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("ResProducts")
                .whereEqualTo("res_id", id)
                .whereEqualTo("completed", completed)
                .whereEqualTo("saleDateYear", year)
                .whereEqualTo("saleDateMonth", month)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                sum.add(restaurantProductModel.cost);
                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    /*
    입력: uid와 completed 상태, 어제 년 월 일.
    출력: 없음.
    동작: 오늘 번 돈을 구합니다.
     */

    public static void getTotalCost(final String id, final int completed, final MyCallback myCallback) {
        final ArrayList<Integer> sum=new ArrayList<>();
        db.collection("ResProducts")
                .whereEqualTo("res_id", id)
                .whereEqualTo("completed", completed)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                sum.add(restaurantProductModel.cost);

                            }
                            myCallback.onSuccess(sum);
                        } else {
                            myCallback.onFail(1, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    /*
    입력: uid와 completed 상태, 어제 년 월 일.
    출력: 없음.
    동작: 전체 돈을 구합니다.
     */
}
