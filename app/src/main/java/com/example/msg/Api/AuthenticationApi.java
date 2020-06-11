package com.example.msg.Api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationApi {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static boolean isLogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) return true;
        else return false;
    }
     //로그인이 된 상태면 true를 아니면 false를 돌려주는 가벼운 함수입니다.

    public static String getCurrentUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }
    //로그인이 된 상태라면 현재 유저의 uid를 스트링 형태로 돌려주는 가벼운 함수입니다.

}
