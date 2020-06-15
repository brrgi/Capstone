package com.example.msg;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        CommonTestFunction tf = CommonTestFunction.getInstance();
        tf.commonLoginSetup(false);
        tf.waitForFirebase(1000);
        //유저 1로 로그인을 한다.
        //유저2를 타겟으로 채팅방을 생성한다.
        //채팅을 하나 친다.
        //유저 2로 로그인한다.
        //유저 2에게 정상적으로 채팅방이 생성됐나 확인한다.
    }

    @Test
    public void testChatRoom() {
        assertEquals(1+1, 5);
    }

    private



}
