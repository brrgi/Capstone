package com.example.msg.ChatRoom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ChatListSqlManager {
    private static  SQLiteDatabase database = null;
    private final String CHAT_LIST_TABLE_NAME = "chat_list";
    private final String DATABASE_NAME = "msg.db";
    private Context context;

    private final String COLUMN1_OPPONENT = "opponent";
    private final String COLUMN2_USER_NAME = "user_name";
    private final String COLUMN3_LAST_DATE = "date";
    private final String COLUMN4_LAST_CHAT_CONTENT = "chat_content";
    private final String COLUMN5_PICTURE = "picture_url";
    private final String COLUMN6_MY_ID = "me";

    public void createDatabase(Context context) {
        if(database == null)  database = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
    }


    public void createTable() {
        if(database == null) {
            Log.d("SQLiteError", "plz make database first");
            return;
        }

        database.execSQL(
                "create table if not exists " + CHAT_LIST_TABLE_NAME + "("
                        + COLUMN1_OPPONENT + " text, "
                        + COLUMN2_USER_NAME + " text, "
                        + COLUMN3_LAST_DATE + " text, "
                        + COLUMN4_LAST_CHAT_CONTENT + " text, "
                        + COLUMN5_PICTURE + " text, "
                        + COLUMN6_MY_ID + " text, " +
                        "PRIMARY KEY(" + COLUMN1_OPPONENT + ", " +  COLUMN6_MY_ID + ")" +
                        ")"
        );
    }

    public void insertRecord(ChatRoomModel chatRoomModel) {
        if(database == null) {
            Log.d("SQLiteError", "plz make database first");
            return;
        }

        database.execSQL(
                "insert or replace into " + CHAT_LIST_TABLE_NAME
                        + " values"
                        + "("
                        + "\""+ chatRoomModel.opponentId + "\"" + ", "
                        + "\""+ chatRoomModel.opponentName + "\""+", "
                        + "\"" + chatRoomModel.lastDate +  "\"" + ", "
                        + "\"" + chatRoomModel.lastChat +  "\"" + ", "
                        + "\"" + chatRoomModel.pictureUrl +  "\"" + ", "
                        + "\"" + chatRoomModel.myId + "\""
                        + ")"
        );
    }

    public ArrayList<ChatRoomModel> executeQuery() {
        ArrayList<ChatRoomModel> chatListDataModels = new ArrayList<>();
        ChatRoomModel chatListDataModel;

        Cursor cursor = database.rawQuery(
                "select * from " + CHAT_LIST_TABLE_NAME
                , null
        );

        int recordCount = cursor.getCount();

        for(int i =0; i < recordCount; i++) {
            cursor.moveToNext();
            chatListDataModel = new ChatRoomModel();
            chatListDataModel.opponentId = cursor.getString(0);
            chatListDataModel.opponentName = cursor.getString(1);
            chatListDataModel.lastDate = cursor.getString(2);
            chatListDataModel.lastChat = cursor.getString(3);
            chatListDataModel.pictureUrl = cursor.getString(4);
            chatListDataModel.myId = cursor.getString(5);

            chatListDataModels.add(chatListDataModel);
        }

        return chatListDataModels;
    }

    public void makeDummyChatList(Context context) {
        ChatListSqlManager chatListSqlManager = new ChatListSqlManager();
        chatListSqlManager.createDatabase(context);
        chatListSqlManager.createTable();
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.myId = "3IWGAGipL2WwCQXhDPfPxjxYlkC3";
        chatRoomModel.pictureUrl = "";
        chatRoomModel.opponentName = "상대: test3@gmail.com";
        chatRoomModel.opponentId = "9mWEviyF0JZT15WXksTPM82JKlB3";
        chatRoomModel.lastDate = "06/11";
        chatRoomModel.lastChat = "아무 말도 안했음.";
        chatListSqlManager.insertRecord(chatRoomModel);
        chatRoomModel.myId = "9mWEviyF0JZT15WXksTPM82JKlB3";
        chatRoomModel.opponentId = "3IWGAGipL2WwCQXhDPfPxjxYlkC3";
        chatRoomModel.opponentName = "상대: test1234@naver.com";
        chatListSqlManager.insertRecord(chatRoomModel);
    }
}
