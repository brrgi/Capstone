package com.example.msg;

import android.util.Log;

import com.example.msg.Api.AuthenticationApi;

public class CommonTestFunction {
    private boolean lock = false;
    private static CommonTestFunction instance = null;
    private String dummyUserId1;
    private String dummyUserUid1;
    private String dummyUserId2;
    private String dummyUserUid2;

    public String getDummyUserId1() {
        return dummyUserId1;
    }

    public String getDummyUserUid1() {
        return dummyUserUid1;
    }

    public String getDummyUserId2() {
        return dummyUserId2;
    }

    public String getDummyUserUid2() {
        return dummyUserUid2;
    }

    public String getDummyResId() {
        return dummyResId;
    }

    public String getDummyResUid() {
        return dummyResUid;
    }

    private String dummyResId;
    private String dummyResUid;
    private final String dummyPassword = "123123";

    public CommonTestFunction(String testerName) {
        switch(testerName) {
            case "천윤서":
                dummyUserId1 = "tdduser01@naver.com";
                dummyUserUid1 = "INFNvZwduOeuaw2VVDKiHZ20Yky2";
                dummyUserId2 = "tdduser02@naver.com";
                dummyUserUid2 = "19YNBgzwMld0s4Q7sadLS0Mh2bg2";
                dummyResId = "";
                dummyResUid = "";
                break;
            case "이레":
                dummyUserId1 = "";
                dummyUserUid1 = "3";
                dummyUserId2 = "";
                dummyUserUid2 = "";
                dummyResId = "";
                dummyResUid = "";
                break;
            case "박규동":
                dummyUserId1 = "";
                dummyUserUid1 = "2";
                dummyUserId2 = "";
                dummyUserUid2 = "";
                dummyResId = "";
                dummyResUid = "";
                break;
            case "한우석":
                dummyUserId1 = "1";
                dummyUserUid1 = "";
                dummyUserId2 = "";
                dummyUserUid2 = "";
                dummyResId = "";
                dummyResUid = "";
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static CommonTestFunction getInstance(String testerName) {
        if(instance == null) instance = new CommonTestFunction(testerName);
        return instance;
    }

    public void commonLoginSetup(boolean isRestaurant, int dummyNumber) {
        String id, pw;

        if(isRestaurant) {
            id = dummyResId;
        }
        else {
            if(dummyNumber == 1) id = dummyUserId1;
            else id = dummyUserId2;
        }
        pw = dummyPassword;

        lock();
        AuthenticationApi.login(id, pw, new AuthenticationApi.MyCallback() {
            @Override
            public void onSuccess() {
                unlock();
            }
            @Override
            public void onFail(int errorCode, Error e) {
                unlock();
            }
        });
        waitUnlock();
        waitForFirebase(3000);

    }
    /*
    파이어베이스 더미 유저 계정의 로그인을 시도합니다. 로그인을 하지 않으면 보안 규칙 때문에 데이터베이스에 접근할 수 없습니다.
    일부 기능들은 유저 계정과 식당 계정의 기능이 다르게 동작하기 때문에, 반드시 유저를 구분해서 테스트하는 것을 권장합니다.
     */

    public void waitUnlock(int timeout) {
        while(lock) {
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {

            }
            timeout -= 100;
            if(timeout < 0) break;
        }
    }

    public void lock() {
        lock = true;
    }

    public void unlock() {
        lock = false;
    }

    public void waitForFirebase(int millisecond) {
        try {
            Thread.sleep(millisecond);
        }
        catch(Exception e) {

        }
    }
    /*
    밀리초를 입력으로 받고, 간단하게 기다리는 함수입니다. 파이어베이스는 비동기 동작을 취하기 떄문에, 안드로이드 앱 상에서는
    처리 과정에서는 버튼을 막거나 하는 식으로 처리할 수 있지만, JUnit에서 그런 복잡한 동작을 구현하는 것은 어렵습니다.
    따라서 간격을 주는 방식을 이용해서 이를 대체하는 것을 권장합니다. 물론, 다중콜백을 사용하는 방식도 있으나, 이는
    테스트 코드의 가독성을 해치기 떄문에 사용하는 것을 권장하지 않습니다.
     */
}
