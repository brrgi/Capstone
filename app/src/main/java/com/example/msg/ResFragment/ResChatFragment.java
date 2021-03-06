package com.example.msg.ResFragment;


import android.content.Context;
import android.os.Bundle;
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
import com.example.msg.ChatRoom.ChatRoomAdapter;
import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.R;

import java.util.ArrayList;


public class ResChatFragment extends Fragment {
    private View view;
    private static final String TAG = "ChatFragment";


    //리사이클러 뷰 관련 변수들.
    private ArrayList<ChatRoomModel> chatRoomModelArrayList;
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;

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

    private void initializeLayout(View view) {
        chatRoomModelArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        loadChatRoomDataAndSetRecyclerView();

    }

    private void loadChatRoomDataAndSetRecyclerView() {
        String myId = AuthenticationApi.getCurrentUid();
        ChatRoomApi.getChatRoomById(myId, new ChatRoomApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ChatRoomModel> chatRoomModels) {
                chatRoomModelArrayList.addAll(chatRoomModels);
                chatRoomAdapter = new ChatRoomAdapter(chatRoomModelArrayList);
                recyclerView.setAdapter(chatRoomAdapter);
                chatRoomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }
    /*
    채팅룸의 데이터를 불러오고 리사이클러뷰와 연결시키는 함수입니다.
     */
}


