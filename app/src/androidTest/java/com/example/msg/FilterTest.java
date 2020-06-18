package com.example.msg;

import android.content.Context;
import android.location.Location;

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
public class FilterTest {

    private CommonTestFunction commonTestFunction = new CommonTestFunction("천윤서");

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.msg", appContext.getPackageName());
    }

    @Test
    public void testSortingByDistance() {
        float[] result = new float[10];
        Location.distanceBetween(37.239624, 126.965566, 37.242331,126.966220, result); //305미터

        assertEquals(1.0, result[0], 1);
    }

    @Test
    public void testSortingByPrice() {

    }

    @Test
    public void testSortingByStock() {

    }

    @Test
    public void testFilteringByCategory() {

    }

    @Test
    public void testFilteringByDistance() {

    }

    @Test
    public void testFilteringByPrice() {

    }

    @Test
    public void testFilteringByQuality() {

    }


}
