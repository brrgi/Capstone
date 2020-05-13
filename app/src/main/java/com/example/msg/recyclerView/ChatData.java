package com.example.msg.recyclerView;

public class ChatData {
    private String userName=null;
    private String message=null;

    public ChatData(){  //필수인듯

    }

    public ChatData(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
