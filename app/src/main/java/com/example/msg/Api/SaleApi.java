package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.SaleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SaleApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(SaleModel saleModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<SaleModel> saleModels);
        void onFail(int errorCode, Exception e);
    }

    public static void postSale(final SaleModel saleModel, final MyCallback myCallback) {
        saleModel.sales_date = new Timestamp(new Date());

        db.collection("Sales").add(saleModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        db.collection("Sales").document(documentReference.getId())
                                .update("sales_id",documentReference.getId());

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

    public static void getSaleByResId(final String resId,  final MyListCallback myCallback) {
        db.collection("Sales")
                .whereEqualTo("res_id", resId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SaleModel saleModel = null;
                        ArrayList<SaleModel> saleModelArrayList = new ArrayList<SaleModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                saleModel = document.toObject(SaleModel.class);
                                saleModelArrayList.add(saleModel);
                            }
                            myCallback.onSuccess(saleModelArrayList);

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
    입력: ID
    출력: 없음.
    동작: ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델들의 리스트를 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다. 실패시 onFail이 호출됩니다.
     */

    public static void getSaleByUserId(final String userId,  final MyListCallback myCallback) {
        db.collection("Sales")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SaleModel saleModel = null;
                        ArrayList<SaleModel> saleModelArrayList = new ArrayList<SaleModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                saleModel = document.toObject(SaleModel.class);
                                saleModelArrayList.add(saleModel);
                            }
                            myCallback.onSuccess(saleModelArrayList);

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
    입력: ID
    출력: 없음.
    동작: ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델들의 리스트를 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다. 실패시 onFail이 호출됩니다.
     */

    public static void updateSales(final SaleModel saleModel, final MyCallback myCallback) {
        db.collection("Sales").document(saleModel.sales_id).
                update(
                        "review", saleModel.review
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(saleModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }

    public static void getSaleByProductIdandUserId(final String user_id, final String product_id,  final MyListCallback myCallback) {
        db.collection("Sales")
                .whereEqualTo("product_id", product_id)
                .whereEqualTo("user_id",user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SaleModel saleModel = null;
                        ArrayList<SaleModel> saleModelArrayList = new ArrayList<SaleModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                saleModel = document.toObject(SaleModel.class);
                                saleModelArrayList.add(saleModel);
                            }
                            myCallback.onSuccess(saleModelArrayList);

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
}


