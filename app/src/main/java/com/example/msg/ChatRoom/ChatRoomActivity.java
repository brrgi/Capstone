package com.example.msg.ChatRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ChatApi;
import com.example.msg.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private String myId = "1";
    private String opponentId = "2";
    private String opponentName = "김철수";

    //리사이클러뷰 관련 요소들.
    private ArrayList<Chat> chats;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;


    private Button sendButton;
    private EditText sendEditText;

    private void initializeLayout() {


        sendButton = findViewById(R.id.chatRoomActivity_button_send);
        sendEditText = findViewById(R.id.chatRoomActivity_editText_send);

        //리사이클러뷰 관련 초기화 및 RealTimeDatabase와 연동.
        chats = new ArrayList<>();
        recyclerView = findViewById(R.id.chatRoomActivity_recyclerView_chatViewer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatAdapter = new ChatAdapter(chats);
        recyclerView.setAdapter(chatAdapter);

        ChatApi.setChatListener(chats, myId, opponentId, new ChatApi.MyCallback() {
            @Override
            public void execute() {
                chatAdapter.notifyDataSetChanged();
                scrollToEnd();
            }
        });

    }

    private void initializeId() {
        myId = AuthenticationApi.getCurrentUid();
        Intent intent = getIntent();

        opponentId = intent.getExtras().getString("id");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initializeId();
        initializeLayout();

        //버튼을 누르면 EditText의 내용을 채팅서버로 전송한다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatApi.postChat(myId, opponentId, sendEditText.getText().toString());
                scrollToEnd();
                sendEditText.setText("");
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.lastChat = chats.get(chats.size() -1).takeContent();
        chatRoomModel.lastDate = chats.get(chats.size() -1).getDate();
        chatRoomModel.myId = myId;
        chatRoomModel.opponentId = opponentId;
        chatRoomModel.opponentName = opponentName;
        chatRoomModel.pictureUrl = "";

        ChatListSqlManager chatListSqlManager = new ChatListSqlManager();
        chatListSqlManager.createDatabase(getApplicationContext());
        chatListSqlManager.createTable();
        chatListSqlManager.insertRecord(chatRoomModel);
        */

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void scrollToEnd() {
        if(chatAdapter.getItemCount() != 0) recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
    }
    //리사이클러뷰의 스크롤을 현재 마지막 아이템으로 내려주는 시스템.
}
