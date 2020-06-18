package com.example.msg;

import android.content.Context;
import android.location.Location;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.msg.Api.DistanceApi;

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
