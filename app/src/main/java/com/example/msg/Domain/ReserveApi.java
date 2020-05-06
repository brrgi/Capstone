package com.example.msg.Domain;

import com.example.msg.DatabaseModel.ReserveModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReserveApi {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();


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
    

    public static void getReservation() {

    } //사용자 정보에서 내 예약 리스트 확인

}
