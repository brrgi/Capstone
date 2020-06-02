package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public static void postReservation(final ReserveModel reserveModel, final MyCallback myCallback) {
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
    //TODO 아래의 postReservation2 함수를 이것으로 대체할것.


    public static void postReservation2(ReserveModel reserveModel) {
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


    private static String makeCurrentTimeString() {
        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("YY.MM.DD");
        return timeFormat.format(now);
    }

    public static void getReservationListById(final String id, final MyListCallback myCallback) {
        db.collection("Reservations")
                .whereEqualTo("user_id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ReserveModel reserveModel = null;
                        ArrayList<ReserveModel> reserveModels = new ArrayList<ReserveModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                reserveModel = document.toObject(ReserveModel.class);
                                reserveModels.add(reserveModel);
                            }
                            myCallback.onSuccess(reserveModels);

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
    public static void deleteReservationByReserveId(final String reserveId, final MyCallback myCallback) {
        db.collection("Reservations").document(reserveId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            myCallback.onSuccess(null);
                        }
                        else {
                            myCallback.onFail(1,null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(3,e);

            }
        });
    }
}


