package com.example.msg.ChatRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.msg.Api.ChatApi;
import com.example.msg.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private String myId = "2";
    private String opponentId = "1";

    //리사이클러뷰 관련 요소들.
    private ArrayList<Chat> chats;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;


    private Button sendButton;
    private EditText sendEditText;

    private void initializeLayout() {

        sendButton = findViewById(R.id.chatRoomActivity_button_send);
        sendEditText = findViewById(R.id.chatRoomActivity_editText_send);

        //리사이클러뷰 관련 초기화 및 RealTimeDatabase와 연동.
        chats = new ArrayList<>();
        recyclerView = findViewById(R.id.chatRoomActivity_recyclerView_chatViewer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChatAdapter chatAdapter = new ChatAdapter(chats);
        recyclerView.setAdapter(chatAdapter);

        ChatApi.setChatListener(chats, chatAdapter, myId, opponentId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initializeLayout();

        //버튼을 누르면 EditText의 내용을 채팅서버로 전송한다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatApi.postChat(myId, opponentId, sendEditText.getText().toString());
            }
        });


    }
}
