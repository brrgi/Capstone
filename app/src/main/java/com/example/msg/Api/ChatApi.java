package com.example.msg.Api;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.ChatModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.UserProductModel;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ChatApi {
    public interface MyListCallback {
        void onSuccess(ArrayList<ChatModel> chats);
        void onFail(int errorCode, Exception e);
    }

    public interface MyCallback {
        void onSuccess(ChatModel chatModel);
        void onFail(int errorCode, Exception e);
    }

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void postShare(final ChatModel chatModel , final MyCallback myCallback) {
        chatModel.date = new Timestamp(new Date());

        db.collection("Share").add(chatModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        myCallback.onSuccess(chatModel);
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

    public static void getChatListFromId(final String myId, final String opponentId, Date after, final MyListCallback myListCallback) {
        final ArrayList<ChatModel> chats = new ArrayList<ChatModel>();
        final Timestamp timestamp = new Timestamp(after);

        db.collection("Chat")
                .whereEqualTo("chat_from", myId)
                .whereEqualTo("chat_to", opponentId)
                .whereGreaterThanOrEqualTo("date", timestamp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ChatModel chatModel = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                chatModel = document.toObject(ChatModel.class);
                                chats.add(chatModel);
                            }

                            db.collection("Chat")
                                    .whereEqualTo("chatFrom", opponentId)
                                    .whereEqualTo("chatTo", myId)
                                    .whereGreaterThanOrEqualTo("date", timestamp)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ChatModel chatModel = null;
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    chatModel = document.toObject(ChatModel.class);
                                                    chats.add(chatModel);
                                                }
                                                sortByDate(chats);
                                                myListCallback.onSuccess(chats);
                                                //최종적으로 데이터를 성공적으로 가져온 부분.
                                            }
                                            else {
                                                //태스크 실패.
                                            }
                                        }
                                    });
                        } else {
                            //태스크 실패.
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListCallback.onFail(0, e); //실패.
            }
        });
    }
    /*
    입력: 상대방과 내 id.
    출력: 없음.
    동작: 상대방과 내 id에 일치하는 모델을 전부 끌어옵니다. 그 결과는 myCallback의 onSuccess 안에서 참조할 수 있습니다.
     */

    public static void sortByDate(ArrayList<ChatModel> modelList) {

        Comparator<ChatModel> myComparator = new Comparator<ChatModel>() {
            @Override
            public int compare(ChatModel o1, ChatModel o2) {
                return o1.date.compareTo(o2.date);
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 현재 위치의 GPS값과 userProductModel List
    출력: 없음
    동작 : 입력으로 들어온 UserProductModel의 리스트를 맨허튼 거리 계산법에 따라 가까운 순으로 정렬해줍니다.
     */
}
