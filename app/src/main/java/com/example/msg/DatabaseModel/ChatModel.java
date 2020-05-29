/*
파일명: ChatModel.java
설  명: 이 파일은 Firebase의 FireStore에 사용되는 특정 데이터베이스 모델과 1:1 대응합니다. 작명 규칙은 안드로이드 프로젝트의 것이 아닌
데이터베이스의 모델의 필드를 따르고 있기에 xxx_yyy 식이며(만약 데이터베이스 안의 데이터 모델과 필드 명이 같지 않다면 런타임 에러가 발생
합니다.), 해당 클래스의 용도는 데이터베이스에서 데이터를 불러오기 쉽게 하는 용도 그 이상, 그 이하도 아니기 때문에 생산성을 높이기 위해서
모든 필드가 private이 아닌 public으로 선언되어 있습니다.
 */

package com.example.msg.DatabaseModel;
import com.google.firebase.Timestamp;

public class ChatModel {
    public String chat_from;
    public String chat_to;
    public Timestamp date;
    public Boolean read;
}
