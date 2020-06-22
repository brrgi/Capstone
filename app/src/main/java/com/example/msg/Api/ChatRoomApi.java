package com.example.msg.Api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatRoomApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String pathName = "ChatRoom";

    public interface MyCallback {
        void onSuccess(ChatRoomModel chatRoomModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback {
        void onSuccess(ArrayList<ChatRoomModel>chatRoomModels);
        void onFail(int errorCode, Exception e);
    }

    private static void arrangeModelId(ChatRoomModel chatRoomModel) {
        if(chatRoomModel.id1.compareTo(chatRoomModel.id2) > 0) {

        }
        else {
            String temp;
            temp = chatRoomModel.id1;
            chatRoomModel.id1 = chatRoomModel.id2;
            chatRoomModel.id2 = temp;
        }
    }
    /*
    입출력: 입력은 모델, 출력은 없음.
    동작: ChatRoomModel이 가진 id를 항상 사전순으로 정렬되게 합니다. API의 정상 작동을 보증하기 위해서 항상 외부에서 chatRoomModel을 받을때는
    항상 이 함수를 가장 앞에 써주세요.
     */

    public static void postOrUpdateChatRoom(final ChatRoomModel chatRoomModel, final MyCallback myCallback) {
        arrangeModelId(chatRoomModel);

        db.collection(pathName).document(chatRoomModel.id1 + chatRoomModel.id2).set(chatRoomModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(chatRoomModel);
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
    동작: Model을 받아서 데이터베이스에 추가하는 동작을하고, 이미 있으면 업데이트를 합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.
     */

    public static void getChatRoomById(final String myId, final MyListCallback myListCallback) {
        final ArrayList<ChatRoomModel> chatRoomModels = new ArrayList<>();
        db.collection(pathName).whereEqualTo("id1", myId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ChatRoomModel chatRoomModel = null;

                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        chatRoomModel = documentSnapshot.toObject(ChatRoomModel.class);
                        chatRoomModels.add(chatRoomModel);
                    }

                    db.collection(pathName).whereEqualTo("id2", myId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ChatRoomModel innerChatRoomModel = null;
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    innerChatRoomModel = documentSnapshot.toObject(ChatRoomModel.class);
                                    chatRoomModels.add(innerChatRoomModel);
                                }
                                myListCallback.onSuccess(chatRoomModels);
                            } else {
                                myListCallback.onFail(21, null);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            myListCallback.onFail(20, e);
                        }
                    });


                } else{
                    myListCallback.onFail(1, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 사용자의 id와 콜백함수.
    출력: 없음
    동작: 현재 사용자의 아이디를 받아서 해당 id를 포함하는 ChatRoom정보를 전부 끌어옵니다.
     */

    public static void makeChatRoomModelById(final String opponentId, boolean isOpponentUser, final MyCallback myCallback) {
        final String myId = AuthenticationApi.getCurrentUid();
        final ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.id1 = myId;
        chatRoomModel.id2 = opponentId;

        if(isOpponentUser) {
            UserApi.getUserById(myId, new UserApi.MyCallback() {
                @Override
                public void onSuccess(UserModel userModel) {
                    chatRoomModel.name1 = userModel.user_name;
                    UserApi.getUserById(opponentId, new UserApi.MyCallback() {
                        @Override
                        public void onSuccess(UserModel userModel) {
                            chatRoomModel.name2 = userModel.user_name;
                            chatRoomModel.lastDate = "";
                            chatRoomModel.lastChat = "";
                            myCallback.onSuccess(chatRoomModel);
                        }

                        @Override
                        public void onFail(int errorCode, Exception e) {
                            myCallback.onFail(errorCode, e);
                        }
                    });
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    myCallback.onFail(errorCode, e);
                }
            });
        } else {
            UserApi.getUserById(myId, new UserApi.MyCallback() {
                @Override
                public void onSuccess(UserModel userModel) {
                    chatRoomModel.name1 = userModel.user_name;
                    RestaurantApi.getUserById(opponentId, new RestaurantApi.MyCallback() {
                        @Override
                        public void onSuccess(RestaurantModel restaurantModel) {
                            chatRoomModel.name2 = restaurantModel.res_name;
                            chatRoomModel.lastDate = "";
                            chatRoomModel.lastChat = "";
                            myCallback.onSuccess(chatRoomModel);
                        }

                        @Override
                        public void onFail(int errorCode, Exception e) {
                            myCallback.onFail(errorCode, e);
                        }
                    });
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    myCallback.onFail(errorCode, e);
                }
            });
        }

    }



    public static String getOpponentIdByModel(ChatRoomModel chatRoomModel, String myId) {
        if(chatRoomModel.id1.equals(myId)) return chatRoomModel.id2;
        else return chatRoomModel.id1;
    }
    /*

     */

    public static String getOpponentNameByModel(ChatRoomModel chatRoomModel, String myId) {
        if(chatRoomModel.id1.equals(myId)) return chatRoomModel.name2;
        else return chatRoomModel.name1;
    }

}
