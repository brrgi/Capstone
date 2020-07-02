package com.example.msg;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.DistanceApi;
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
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FilteringSpeedTest {
    private CommonTestFunction commonTestFunction = new CommonTestFunction("천윤서");
    private ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

        //commonSetupFixture: 2000개의 더미 데이터 생성. 함수 실행 한번당 4개의 더미데이터가 생성됩니다.
        for(int i =0; i < 500; i++) {
            restaurantProductModels.addAll(getTestRestaurantProductModelList());
        }
    }

    @After
    public void commonCleaningFixture() {
        //commonCleaningFixture:  필요시 작성
    }

    @Test
    public void worstFilterSpeedTest() {
        //SetupFixture
        long measuredTime;
        long timeout = 3000;

        //Exercise SUT : 복합 필터링 적용
        measuredTime = checkFiltering();

        //Verify OutCome
        Log.d("SortSpeedTest", String.format("Type: Filtering, Time: %d(ms)", measuredTime));
        assertTrue("3초 이하인지 검증", measuredTime < 1000);
    }

    @Test
    public void distanceSortSpeedTest() {
        //SetupFixture
        long measuredTime;
        long timeout = 3000;

        //Exercise SUT1 : 거리순 정렬 시간 측정
        measuredTime = checkDistanceSort();

        //Verify OutCome
        Log.d("SortSpeedTest", String.format("Type: Distance, Time: %d(ms)", measuredTime));
        assertTrue("3초 이하인지 검증", measuredTime < 1000);
    }

    @Test
    public void StockSortSpeedTest() {
        //SetupFixture
        long measuredTime;
        long timeout = 3000;

        //Exercise SUT1 : 재고순 정렬 시간 측정
        measuredTime = checkStockSort();

        //Verify OutCome
        Log.d("SortSpeedTest", String.format("Type: Stock, Time: %d(ms)", measuredTime));
        assertTrue("3초 이하인지 검증", measuredTime < 1000);
    }

    @Test
    public void PriceSortSpeedTest() {
        //SetupFixture
        long measuredTime;
        long timeout = 3000;

        //Exercise SUT1 : 가격순 정렬 시간 측정
        measuredTime = checkPriceSort();

        //Verify OutCome
        Log.d("SortSpeedTest", String.format("Type: Price, Time: %d(ms)", measuredTime));
        assertTrue("3초 이하인지 검증", measuredTime < 1000);
    }


    private long checkFiltering() {
        long beforeTime = System.currentTimeMillis();
        RestaurantProductApi.filterByCategory(restaurantProductModels, "딸기");
        RestaurantProductApi.filterByDistance(restaurantProductModels, 0, 0, 100);
        RestaurantProductApi.filterByPrice(restaurantProductModels, 5000);
        RestaurantProductApi.filterByQuality(restaurantProductModels, true, false, false );
        long afterTime = System.currentTimeMillis();
        return afterTime-beforeTime;
    }

    private long checkDistanceSort() {
        long beforeTime = System.currentTimeMillis();
        RestaurantProductApi.sortByDistance(restaurantProductModels, 0, 0);
        long afterTime = System.currentTimeMillis();
        return afterTime-beforeTime;
    }

    private long checkStockSort() {
        long beforeTime = System.currentTimeMillis();
        RestaurantProductApi.sortByStock(restaurantProductModels);
        long afterTime = System.currentTimeMillis();
        return afterTime-beforeTime;
    }

    private long checkPriceSort() {
        long beforeTime = System.currentTimeMillis();
        RestaurantProductApi.sortByPrice(restaurantProductModels);
        long afterTime = System.currentTimeMillis();
        return afterTime-beforeTime;
    }



    private ArrayList<RestaurantProductModel> getTestRestaurantProductModelList() {
        ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();
        Random random = new Random();

        restaurantProductModels.add(getDummyRestaurantProductModel("닭고기입니다.", random.nextInt(100000), random.nextInt(10000), random.nextInt(10000), "닭고기", random.nextInt(3)+1));
        restaurantProductModels.add(getDummyRestaurantProductModel("양파양파.", random.nextInt(100000), random.nextInt(10000), random.nextInt(10000), "양파", random.nextInt(3)+1));
        restaurantProductModels.add(getDummyRestaurantProductModel("성난감자.", random.nextInt(100000), random.nextInt(10000), random.nextInt(10000), "감자", random.nextInt(3)+1));
        restaurantProductModels.add(getDummyRestaurantProductModel("딸기", random.nextInt(100000), random.nextInt(10000), random.nextInt(10000), "딸기", random.nextInt(3)+1));
        restaurantProductModels.get(3).fast = true;
        return restaurantProductModels;
    }

    private RestaurantProductModel getDummyRestaurantProductModel(String title, int cost, int distance, int stock, String categorySmall, int quality) {
        RestaurantProductModel restaurantProductModel = new RestaurantProductModel();
        restaurantProductModel.cost = cost;
        restaurantProductModel.stock = stock;
        restaurantProductModel.latitude = DistanceApi.meterToLatitude(distance);
        restaurantProductModel.longitude = 0;
        restaurantProductModel.expiration_date = "2020-02-02";
        restaurantProductModel.quantity = "100g";
        restaurantProductModel.quality = quality;
        restaurantProductModel.categoryBig = "육류";
        restaurantProductModel.categorySmall = categorySmall;
        restaurantProductModel.p_description = "테스트 데이터입니다.";
        restaurantProductModel.title = title;
        restaurantProductModel.fast = false;
        restaurantProductModel.p_imageURL = null;
        restaurantProductModel.saleDateDay = 0;
        restaurantProductModel.saleDateMonth = 0;
        restaurantProductModel.saleDateYear = 0;
        return restaurantProductModel;
    }
    //사용하는 변수들만 초기화하고 나머지 데이터들은 공통으로 가져감.


}
