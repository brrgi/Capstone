package com.example.msg;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.SubscriptionApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SubscriptionModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GetProductSpeedTest {
    private CommonTestFunction commonTestFunction = new CommonTestFunction("천윤서");
    private ArrayList<String> productId = new ArrayList<>();
    private ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

        //common setup fixture: 데이터베이스 접근 권한을 위해 로그인 합니다.
        commonTestFunction.commonLoginSetup(true, 1);
    }

    @After
    public void commonCleaningFixture() {
        //commonCleaningFixture:  로그아웃 합니다.
        AuthenticationApi.logout();
    }

    @Test
    public void speedTest() {
        int numOfProduct = 1000;

        //Setup Fixture: 상품을 nunOfProduct만큼 올립니다.
        uploadDummyModelBySignedUser(numOfProduct);

        //Exercise SUT: 올라간 상품을 모조리 불러오고 불러온 지연시간을 측정합니다.
        long num = checkGetProductTime();

        //Verify OutCome
        Log.d("OnCycleTest", String.format("Size: %d, Time: %d(ms)", 1000, num));
        assertTrue("3초 이하인지 검증", num < 3000);


        //Cleaning Fixture: 상품을 삭제합니다.
        deleteDummyModelBySignedUser();

    }

    private void uploadDummyModelBySignedUser(int numOfProduct) {

        for(int i =0; i <numOfProduct; i++) {
            restaurantProductModels.add(makeDummyModel());
        }

        for(int i =0; i < restaurantProductModels.size(); i++) {
            commonTestFunction.lock();
            RestaurantProductApi.postProductWithNoImage(restaurantProductModels.get(i), new RestaurantProductApi.MyCallback() {
                @Override
                public void onSuccess(RestaurantProductModel restaurantProductModel) {
                    productId.add(restaurantProductModel.rproduct_id); //나중에 지우기 위해서 아이디를 받아서 보관합니다.
                    commonTestFunction.unlock();
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    commonTestFunction.unlock();
                }
            });
            commonTestFunction.waitUnlock(5000);
        }
    }

    private RestaurantProductModel makeDummyModel() {
        RestaurantProductModel restaurantProductModel = new RestaurantProductModel();
        return restaurantProductModel;
    }

    private void deleteDummyModelBySignedUser() {
        for(int i =0; i < productId.size(); i++) {
            commonTestFunction.lock();
            RestaurantProductApi.deleteProduct(productId.get(i), new RestaurantProductApi.MyCallback() {
                @Override
                public void onSuccess(RestaurantProductModel restaurantProductModel) {
                    commonTestFunction.unlock();
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    commonTestFunction.unlock();
                }
            });
            commonTestFunction.waitUnlock(5000);
        }
    }

    private long checkGetProductTime() {
        long beforeTime = System.currentTimeMillis();
        commonTestFunction.lock();
        RestaurantProductApi.getProductList(0, 0, 100, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(100000000);
        long AfterTime = System.currentTimeMillis();
        return AfterTime - beforeTime;
    }

}
