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
}