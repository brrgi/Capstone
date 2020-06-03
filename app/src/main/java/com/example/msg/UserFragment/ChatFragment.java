package com.example.msg.UserFragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.ChatRoom.Chat;
import com.example.msg.ChatRoom.ChatListSqlManager;
import com.example.msg.ChatRoom.ChatRoomAdapter;
import com.example.msg.ChatRoom.ChatRoomModel;
import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    private View view;
    private static final String TAG = "ChatFragment";

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    //리사이클러 뷰 관련 변수들.
    private ArrayList<ChatRoomModel> chatRoomModels;
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;

    private void initializeLayout(View view) {
        ChatListSqlManager db = new ChatListSqlManager();
        db.createDatabase(view.getContext());
        db.createTable();
        db.makeDummyChatList(view.getContext());
        //loadChatRoomDataFromLocalDatabase(view.getContext()); //로컬DB에서 데이터를 뽑아서 chatRoomModels에 삽입함.
        makeDummyChatData();

        //리사이클러뷰 관련 설정.
        recyclerView = view.findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatRoomAdapter = new ChatRoomAdapter(chatRoomModels);
        recyclerView.setAdapter(chatRoomAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chat,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = view.getContext();

        initializeLayout(view);


    }

    private void makeDummyChatData() {
        String uid = AuthenticationApi.getCurrentUid();
        chatRoomModels = new ArrayList<>();
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        switch(uid) {
            case "PGfIRaSUdGVoJFR188PNloAqfOr1":
                //user1
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.myId = uid;
                chatRoomModel.opponentId = "VJMsi2PlekUObxsRTO58EZPWhyM2";
                chatRoomModel.opponentName = "한우석";
                chatRoomModel.lastDate = "17:23";
                chatRoomModel.lastChat = "안녕하세요!";
                chatRoomModels.add(chatRoomModel);
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.opponentName = "천윤서";
                chatRoomModel.lastDate = "05:05";
                chatRoomModel.lastChat = "네고요.";
                chatRoomModels.add(chatRoomModel);
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.opponentName = "이레";
                chatRoomModel.lastDate = "05:03";
                chatRoomModel.lastChat = "닭가슴살 언제 줘요.";
                chatRoomModels.add(chatRoomModel);
                break;
            case "VJMsi2PlekUObxsRTO58EZPWhyM2":
                //user2
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.myId = uid;
                chatRoomModel.opponentId = "PGfIRaSUdGVoJFR188PNloAqfOr1";
                chatRoomModel.opponentName = "박규동";
                chatRoomModel.lastDate = "17:23";
                chatRoomModel.lastChat = "..";
                chatRoomModels.add(chatRoomModel);
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.opponentName = "이레";
                chatRoomModel.lastDate = "05:11";
                chatRoomModel.lastChat = "잘받았습니다.";
                chatRoomModels.add(chatRoomModel);
                chatRoomModel = new ChatRoomModel();
                chatRoomModel.opponentName = "김영휘";
                chatRoomModel.lastDate = "05:33";
                chatRoomModel.lastChat = "저기요.";
                chatRoomModels.add(chatRoomModel);
                break;
            default:
                break;
        }
    }

    private void loadChatRoomDataFromLocalDatabase(Context context) {
        ChatListSqlManager sql = new ChatListSqlManager();
        sql.createDatabase(context);
        sql.createTable();
        chatRoomModels = sql.executeQuery();
        for(int i =0; i < chatRoomModels.size(); i++) {
            Log.d("ChatTest", chatRoomModels.get(i).opponentId);
        }
    }

}
