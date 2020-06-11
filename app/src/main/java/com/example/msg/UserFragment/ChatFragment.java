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
import com.example.msg.Api.ChatRoomApi;
import com.example.msg.ChatRoom.ChatListSqlManager;
import com.example.msg.ChatRoom.ChatRoomAdapter;
import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.R;
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
    private ArrayList<ChatRoomModel> chatRoomModelArrayList;
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;

    private void initializeLayout(View view) {
        //ChatListSqlManager db = new ChatListSqlManager();
       // db.createDatabase(view.getContext());
       // db.createTable();
       // db.makeDummyChatList(view.getContext());
        //loadChatRoomDataFromLocalDatabase(view.getContext()); //로컬DB에서 데이터를 뽑아서 chatRoomModels에 삽입함.
       // makeDummyChatData();
        loadChatRoomDataFromDatabase();

        //리사이클러뷰 관련 설정.
        recyclerView = view.findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatRoomAdapter = new ChatRoomAdapter(chatRoomModelArrayList);
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

    private void loadChatRoomDataFromDatabase() {
        String myId = AuthenticationApi.getCurrentUid();
        ChatRoomApi.getChatRoomById(myId, new ChatRoomApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ChatRoomModel> chatRoomModels) {
                chatRoomModelArrayList.addAll(chatRoomModels);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }


/*
    private void loadChatRoomDataFromLocalDatabase(Context context) {
        ChatListSqlManager sql = new ChatListSqlManager();
        sql.createDatabase(context);
        sql.createTable();
        chatRoomModels = sql.executeQuery();
        for(int i =0; i < chatRoomModels.size(); i++) {
            Log.d("ChatTest", chatRoomModels.get(i).opponentId);
        }
    }
*/
}
