package com.example.msg.ChatRoom;

import com.example.msg.DatabaseModel.ChatModel;

public class Chat {
    private ChatModel chatModel;
    private String chatId;

    public Chat(ChatModel chatModel, String chatId) {
        this.chatId = chatId;
        this.chatModel = chatModel;
    }

    public void modifyChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getChatId() {
        return chatId;
    }

    public String getDate() {
        return chatModel.date;
    }

    public boolean isRead() {
        return chatModel.read;
    }

    public boolean isMine(String myId) {
        if(myId.equals(chatModel.chat_from)) return true;
        else return false;
    }

    public String takeContent() {
        return chatModel.content;
    }
}
