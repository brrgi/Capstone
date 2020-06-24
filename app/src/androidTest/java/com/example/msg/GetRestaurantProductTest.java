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
    private ArrayList<String> subscriptionIds = new ArrayList<>();
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
        assertEquals("Setup이 네트워크 문제 등으로 되지 않으면 테스트가 진행되는 것을 막습니다.",3, restaurantProductModels.size());
        AuthenticationApi.logout();

    }

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
                    Log.d("logofrpt", String.format("here fail: %d", errorCode));
                    if(e != null) Log.d("logofrpt", "error msg is:" + e.getMessage());
                }
            });
            Log.d("logofrpt", "here wait");
            commonTestFunction.waitUnlock(5000);
        }
        AuthenticationApi.logout();
    }

    @Test
    public void testSubscribe() {
        //Setup Fixture: 로그인 이후 더미 구독 모델 생성.
        ArrayList<RestaurantProductModel> extractedModel = new ArrayList<>();
        boolean isSuccess;

        commonTestFunction.commonLoginSetup(false, 1);
        final SubscriptionModel subscriptionModel = getDummySubscriptionModel();

        //Exercise SUT Step1: 더미 구독 모델로 식당을 구독.
        isSuccess = subscribeRestaurantWithSpinLock(subscriptionModel);

        //Verify OutCome
        assertEquals(true , isSuccess);

        //Exercise SUT Step2: 자신이 구독한 식당의 모델들만 따로 반환.
        extractedModel.addAll(extractSubscribedModelWithSpinlock(restaurantProductModels));

        //Verify OutCome
        for(int i = 0; i < extractedModel.size(); i++) {
            Log.d("임시테스트", extractedModel.get(i).categorySmall);
        }
        assertEquals("구독된 식당의 상품은 양파와 닭고기만 있음", 2, extractedModel.size());
        assertEquals("구독 안된 식당의 상품은 양배추만 있음", 1, restaurantProductModels.size());

        String[] noSubscribed = {"양배추"};
        String[] subscribed = {"양파", "닭고기"};

        assertContain("구독된 식당의 상품은 양파와 닭고기만 있음", extractedModel, subscribed);
        assertContain("구독 안된 식당의 상품은 양배추만 있음", restaurantProductModels, noSubscribed);

        //Cleaning Fixture
        unSubscribe();
        AuthenticationApi.logout();
    }

    @Test
    public void getProductInformationTest() {

    }

    @Test
    public void EvaluateTest() {

    }

    private void unSubscribe() {
        commonTestFunction.lock();
        SubscriptionApi.deleteSubscriptionBySubsId(subscriptionIds.get(0), new SubscriptionApi.MyCallback() {
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
    }

    private ArrayList<RestaurantProductModel> extractSubscribedModelWithSpinlock(final ArrayList<RestaurantProductModel> restaurantProductModels) {
        final ArrayList<RestaurantProductModel> extractedModel = new ArrayList<>();
        commonTestFunction.lock();
        RestaurantProductApi.extractSubscribedModel(restaurantProductModels, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                extractedModel.addAll(restaurantModelArrayList);
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(5000);
        return extractedModel;
    }

    private boolean subscribeRestaurantWithSpinLock(SubscriptionModel subscriptionModel) {

        final boolean[] isSuccess = new boolean[1];
        isSuccess[0] = false;
        commonTestFunction.lock();
        SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
            @Override
            public void onSuccess(SubscriptionModel subscriptionModel) {
                isSuccess[0] = true;
                subscriptionIds.add(subscriptionModel.subs_id);
                commonTestFunction.unlock();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                commonTestFunction.unlock();
            }
        });
        commonTestFunction.waitUnlock(5000);
        return isSuccess[0];
    }

    private void assertContain(String msg, ArrayList<RestaurantProductModel> restaurantProductModels, String[] actualExist) {
        boolean flag;
        for(int i =0; i < actualExist.length; i++) {
            flag = false;
            for(int j = 0; j < restaurantProductModels.size(); j++) {
                if(actualExist[i].equals(restaurantProductModels.get(j).categorySmall)) flag = true;
            }
            assertEquals(true, flag);
        }
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
