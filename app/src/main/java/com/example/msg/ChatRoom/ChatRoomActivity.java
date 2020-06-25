/*
채팅 리스트의 채팅을 클릭해서 입장한 채팅방의 액티비티입니다.
 */

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
import com.example.msg.Api.ChatRoomApi;
import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private String myId;
    private String opponentId;
    private ChatRoomModel chatRoomModel;

    //리사이클러뷰 관련 요소들.
    private ArrayList<Chat> chats;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    //기타 레이아웃 관련 요소들.
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
    /*
    레이아웃을 초기화하고 채팅의 실시간 리스너를 등록합니다. 이때문에 UI는 채팅 데이터베이스의 변화에 따라
    실시간으로 변화를 일으킵니다.
     */



    private void initializeId() {
        myId = AuthenticationApi.getCurrentUid();
        Intent intent = getIntent();

        chatRoomModel = (ChatRoomModel)intent.getSerializableExtra("object");
        opponentId = ChatRoomApi.getOpponentIdByModel(chatRoomModel, myId);

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

        try {
            chatRoomModel.lastChat = chats.get(chats.size() -1).takeContent();
            chatRoomModel.lastDate = chats.get(chats.size() -1).getDate();
            chatRoomModel.id1 = myId;
            chatRoomModel.id2 = opponentId;
            chatRoomModel.pictureUrl = "";

            ChatRoomApi.postOrUpdateChatRoom(chatRoomModel, new ChatRoomApi.MyCallback() {
                @Override
                public void onSuccess(ChatRoomModel chatRoomModel) {

                }

                @Override
                public void onFail(int errorCode, Exception e) {

                }
            });
        } catch(Exception e ) {
        }





        /*
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
