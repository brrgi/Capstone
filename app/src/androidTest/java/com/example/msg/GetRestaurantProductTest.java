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
public class GetRestaurantProductTest {
    private ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();
    private ArrayList<String> restaurantProductModelIds = new ArrayList<>();
    private CommonTestFunction commonTestFunction = new CommonTestFunction("천윤서");

    //공통: 식당으로 레스토랑 모델을 등록한다.
    //공통: 식당으로 로그아웃 한다.
    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

        //common setup fixture: 식당으로 로그인 한 뒤 더미 모델을 올리고 다시 로그아웃한다.
        commonTestFunction.commonLoginSetup(true, 0);
        assertEquals(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyResUid());
        restaurantProductModels.addAll(getDummyProductModels());

        for(int i =0; i < restaurantProductModels.size(); i++) {
            commonTestFunction.lock();
            RestaurantProductApi.postProductWithNoImage(restaurantProductModels.get(i), new RestaurantProductApi.MyCallback() {
                @Override
                public void onSuccess(RestaurantProductModel restaurantProductModel) {
                    restaurantProductModelIds.add(restaurantProductModel.rproduct_id); //나중에 지우기 위해서 아이디를 받아서 보관합니다.
                    commonTestFunction.unlock();
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    commonTestFunction.unlock();
                }
            });
            commonTestFunction.waitUnlock(5000);
            Log.d("logofrpt", "here i m");
        }
        assertEquals(3, restaurantProductModels.size());
        AuthenticationApi.logout();

    }

    //공통: 식당으로 로그인한다.
    //공통: 식당으로 레스토랑 모델을 삭제한다.
    //공통: 식당으로 로그아웃 한다.
    @After
    public void commonCleaningFixture() {
        commonTestFunction.commonLoginSetup(true, 0);
        assertEquals(AuthenticationApi.getCurrentUid(), commonTestFunction.getDummyResUid());
        //commonCleaningFixture: 식당으로 로그인 한 뒤 올라간 더미 모델을 전부 지우고 다시 로그아웃한다.
        for(int i =0; i < restaurantProductModelIds.size(); i++) {
            commonTestFunction.lock();
            RestaurantProductApi.deleteProduct(restaurantProductModelIds.get(i), new RestaurantProductApi.MyCallback() {
                @Override
                public void onSuccess(RestaurantProductModel restaurantProductModel) {
                    commonTestFunction.unlock();
                    Log.d("logofrpt", "here delete");
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    commonTestFunction.unlock();
                    Log.d("logofrpt", "here fail");
                }
            });
            Log.d("logofrpt", "here wait");
            commonTestFunction.waitUnlock(5000);
            AuthenticationApi.logout();
        }

    }

    @Test
    public void testSubscribe() {
        //유저로 로그인한다.
        commonTestFunction.commonLoginSetup(false, 1);
        final SubscriptionModel subscriptionModel = getDummySubscriptionModel();

        commonTestFunction.lock();
        SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
            @Override
            public void onSuccess(SubscriptionModel subscriptionModel) {
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(5000);
        //유저로 식당을 구독한다.

        commonTestFunction.lock();
        final ArrayList<SubscriptionModel> subscriptionModels = new ArrayList<>();
        SubscriptionApi.getSubscriptionListByUserId(AuthenticationApi.getCurrentUid(), new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                subscriptionModels.addAll(subscriptionModelArrayList);
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(5000);

        ArrayList<RestaurantProductModel> modelList = new ArrayList<>();
        modelList = RestaurantProductApi.filterBySubscription(restaurantProductModels, subscriptionModels);

        RestaurantProductApi.deleteDuplicated(modelList, restaurantProductModels);

        for(int i = 0; i < modelList.size(); i++) {
            Log.d("logofrpt", modelList.get(i).categorySmall);
        }


        //유저로 식당 필터링을 한다.
        //식당을 구독해제한다.
        //로그아웃한다.


        //ArrayList<RestaurantProductModel> subscribedModel = new ArrayList<>();
        //subscribedModel.add(restaurantProductModels.get(0));

        //RestaurantProductApi.deleteDuplicated(subscribedModel, restaurantProductModels);
        //assertEquals(2, restaurantProductModels.size());

    }

    @Test
    public void getProductInformationTest() {

    }

    @Test
    public void EvaluateTest() {

    }

    private ArrayList<RestaurantProductModel> getDummyProductModels() {
        ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

        RestaurantProductModel restaurantProductModel = new RestaurantProductModel();
        restaurantProductModel.categorySmall = "양파";
        restaurantProductModel.res_id = commonTestFunction.getDummyResUid();
        restaurantProductModel.title = "양파맛";
        restaurantProductModels.add(restaurantProductModel);

        restaurantProductModel = new RestaurantProductModel();
        restaurantProductModel.categorySmall = "양배추";
        restaurantProductModel.res_id = "542323"; //구독을 구분하기 위한 가짜 모델.
        restaurantProductModel.title = "샐러드용 양배추";
        restaurantProductModels.add(restaurantProductModel);

        restaurantProductModel = new RestaurantProductModel();
        restaurantProductModel.categorySmall = "닭고기";
        restaurantProductModel.res_id = commonTestFunction.getDummyResUid();
        restaurantProductModel.title = "닭닭고기";
        restaurantProductModels.add(restaurantProductModel);

        return restaurantProductModels;
    }

    private SubscriptionModel getDummySubscriptionModel() {
        SubscriptionModel subscriptionModel = new SubscriptionModel();
        subscriptionModel.user_id = AuthenticationApi.getCurrentUid();
        subscriptionModel.res_id = commonTestFunction.getDummyResUid();
        return subscriptionModel;
    }

}
