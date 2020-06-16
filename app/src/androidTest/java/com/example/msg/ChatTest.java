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

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

        CommonTestFunction tf = CommonTestFunction.getInstance("천윤서");
        tf.commonLoginSetup(false, 1);
        assertEquals(AuthenticationApi.getCurrentUid(), tf.getDummyUserUid1());
        //유저 1로 로그인을 한다.

    }

    @Test
    public void testChatRoomCreation() {
        //더미유저2를 타겟으로 채팅방을 생성한다.
        final CommonTestFunction tf = CommonTestFunction.getInstance("천윤서");
        makeChatRoom(AuthenticationApi.getCurrentUid(), tf.getDummyUserUid2());

        tf.lock();
        ShareApi.getShareByToId("", new ShareApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ShareModel> shareModelArrayList) {
                tf.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                tf.unlock();
            }
        });
        tf.waitUnlock();
        assertEquals(1+1, 5);


        AuthenticationApi.logout();

        //유저 2로 로그인한다.
        tf.commonLoginSetup(false, 2);
        assertEquals(AuthenticationApi.getCurrentUid(), tf.getDummyUserUid2());


        //유저 2에게 정상적으로 채팅방이 생성됐나 확인한다.
        tf.lock();
        final ArrayList<ChatRoomModel> chatRoomModelArrayList = new ArrayList<>();
        ChatRoomApi.getChatRoomById(AuthenticationApi.getCurrentUid(), new ChatRoomApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ChatRoomModel> chatRoomModels) {
                chatRoomModelArrayList.addAll(chatRoomModels);
                tf.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                tf.unlock();
            }
        });
        tf.waitForFirebase(1000);

        assertEquals(chatRoomModelArrayList.get(0).lastChat, "안녕하세요");

    }



    private void makeChatRoom(String currentUser, String targetUser) {
        final CommonTestFunction tf = CommonTestFunction.getInstance("천윤서");
        tf.lock();
        ChatRoomModel chatRoomModel = makeDummyChatRoom(currentUser, targetUser);
        ChatRoomApi.postOrUpdateChatRoom(chatRoomModel, new ChatRoomApi.MyCallback() {
            @Override
            public void onSuccess(ChatRoomModel chatRoomModel) {
                tf.unlock();
            }
            @Override
            public void onFail(int errorCode, Exception e) {
                tf.unlock();
            }
        });
        tf.waitUnlock();

    }

    private ChatRoomModel makeDummyChatRoom(String id1, String id2) {
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.id1 = id1;
        chatRoomModel.id2 = id2;
        chatRoomModel.lastChat = "안녕하세요";
        chatRoomModel.opponentName = "김규동";
        chatRoomModel.lastDate = "06:02";
        chatRoomModel.pictureUrl = null;
        return chatRoomModel;
    }



}
