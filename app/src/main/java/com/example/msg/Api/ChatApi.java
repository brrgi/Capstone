package com.example.msg.Api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.msg.ChatRoom.Chat;
import com.example.msg.ChatRoom.ChatAdapter;
import com.example.msg.DatabaseModel.ChatModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ChatApi {
    private static DatabaseReference realTime = FirebaseDatabase.getInstance().getReference();
    private final static String childName = "chat";

    public static String makeChatRoomId(String myId, String opponentId) {
        if(myId.compareTo(opponentId) > 0) {
            return myId + opponentId;
        }
        else if(myId.compareTo(opponentId) < 0) {
            return opponentId + myId;
        }
        else {
            return "ERROR";
        }
    }
    /*
    입력:
    출력:
    동작:
     */


    public static void postChat(String myId, String opponentId, String content) {
        String chatRoomId = makeChatRoomId(myId, opponentId);

        ChatModel chatModel = new ChatModel();
        chatModel.chat_from = myId;
        chatModel.chat_to = opponentId;
        chatModel.content = content;
        chatModel.date = makeCurrentTimeString();
        chatModel.read = false;

        realTime.child(childName).child(chatRoomId).push().setValue(chatModel);
    }

    public static void setChatListener(final ArrayList<Chat> chats, final ChatAdapter chatAdapter, final String myId, final String opponentId) {
        String chatRoomId = makeChatRoomId(myId, opponentId);

        realTime.child(childName).child(chatRoomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                Chat chat = new Chat(chatModel, s);
                chats.add(chat);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(int i =0; i < chats.size(); i++) {
                    if(chats.get(i).getChatId().equals(s)) {
                        chats.get(i).modifyChatModel(dataSnapshot.getValue(ChatModel.class));
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static String makeCurrentTimeString() {
        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(now);
    }
    /*
    입력:
    출력:
    동작:
     */





}

