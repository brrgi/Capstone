package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.ShareModel;
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

public class ShareApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MyCallback {
        void onSuccess(ShareModel shareModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<ShareModel> shareModelArrayList);
        void onFail(int errorCode, Exception e);
    }

    public static void postShare(final ShareModel shareModel , final MyCallback myCallback) {
        shareModel.date = new Timestamp(new Date());

        db.collection("Share").add(shareModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        myCallback.onSuccess(shareModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 모델
    출력: 없음.
    동작: 모델을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.
     */

    public static void getShareByFromId(final String id,  final MyListCallback myCallback) {
        db.collection("Share")
                .whereEqualTo("share_from", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ShareModel shareModel = null;
                        ArrayList<ShareModel> shareModelArrayList = new ArrayList<ShareModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shareModel = document.toObject(ShareModel.class);
                                shareModelArrayList.add(shareModel);
                            }
                            myCallback.onSuccess(shareModelArrayList);

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



    public static void getShareByToId(final String id, final MyListCallback myCallback) {
        db.collection("Share")
                .whereEqualTo("share_to", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ShareModel shareModel = null;
                        ArrayList<ShareModel> shareModelArrayList = new ArrayList<ShareModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shareModel = document.toObject(ShareModel.class);
                                shareModelArrayList.add(shareModel);
                            }
                            myCallback.onSuccess(shareModelArrayList);

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
}
