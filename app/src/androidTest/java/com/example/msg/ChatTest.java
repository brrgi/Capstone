package com.example.msg;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ChatRoomApi;
import com.example.msg.Api.ShareApi;
import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.google.android.gms.auth.api.Auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ChatTest {

    private CommonTestFunction commonTestFunction = CommonTestFunction.getInstance("천윤서");

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

        AuthenticationApi.logout();
    }

    @Test
    public void testChatRoomCreation() {

        commonTestFunction.commonLoginSetup(false, 1);
        assertEquals(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyUserUid1());
        //유저 1로 로그인을 한다.

        //더미유저2를 타겟으로 채팅방을 생성한다.
        ChatRoomModel modelForCreation = makeDummyChatRoom(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyUserUid2());
        ChatRoomModel modelForAssert = makeDummyChatRoom(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyUserUid2());
        makeChatRoom(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyUserUid2(), modelForCreation);

        //유저 2로 로그인하고, 로그인이 성공했는지 검증한다.
        commonTestFunction.commonLoginSetup(false, 2);
        assertEquals(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyUserUid2());


        //유저 2의 계정을 이용해서 채팅방을 불러오고, 검증용 모델과 비교하여 정상적으로 채팅방이 생성됐나 확인한다.
        commonTestFunction.lock();
        final ArrayList<ChatRoomModel> chatRoomModelArrayList = new ArrayList<>();
        ChatRoomApi.getChatRoomById(AuthenticationApi.getCurrentUid(), new ChatRoomApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ChatRoomModel> chatRoomModels) {
                chatRoomModelArrayList.addAll(chatRoomModels);
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitForFirebase(2000);
        assertEqualsModel(modelForAssert, chatRoomModelArrayList.get(0));
    }

    private void assertEqualsModel(ChatRoomModel expected, ChatRoomModel actual) {
        assertEquals(expected.lastChat, actual.lastChat);
        assertEquals(expected.lastDate, actual.lastDate);
        //assertEquals(expected.opponentName, actual.opponentName);
    }

    private void makeChatRoom(String currentUser, String targetUser, ChatRoomModel chatRoomModel) {
        commonTestFunction.lock();
        ChatRoomApi.postOrUpdateChatRoom(chatRoomModel, new ChatRoomApi.MyCallback() {
            @Override
            public void onSuccess(ChatRoomModel chatRoomModel) {
                commonTestFunction.unlock();
            }
            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(5000);

    }

    private ChatRoomModel makeDummyChatRoom(String id1, String id2) {
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.id1 = id1;
        chatRoomModel.id2 = id2;
        chatRoomModel.lastChat = "안녕하세요";
        //chatRoomModel.opponentName = "김규동";
        chatRoomModel.lastDate = "06:02";
        chatRoomModel.pictureUrl = null;
        return chatRoomModel;
    }



}
