package com.example.msg;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.msg.Api.DistanceApi;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;

import org.junit.After;
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
public class FilterTest {

    //Common used Test Model.
    private CommonTestFunction commonTestFunction = new CommonTestFunction("천윤서");
    private ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

       //common setup
       restaurantProductModels = new ArrayList<>();
       restaurantProductModels.addAll(getTestRestaurantProductModelList());
    }

    @After
    public void commonTearDown() {
        //필요하면 작성.
    }

    @Test
    public void testSortingByDistance() {
        RestaurantProductApi.sortByDistance(restaurantProductModels, 0, 0);
        //소트 실행.
        assertSortByDistance(restaurantProductModels);
        //검증 실행
    }

    @Test
    public void testSortingByPrice() {
        RestaurantProductApi.sortByPrice(restaurantProductModels);
        //소트 실행.
        assertSortByPrice(restaurantProductModels);
        //검증 실행
    }

    @Test
    public void testSortingByStock() {
        RestaurantProductApi.sortByStock(restaurantProductModels);
        //소트 실행
        assertSortByStock(restaurantProductModels);
        //검증실행
    }



    @Test
    public void testFilteringByCategory() {
        restaurantProductModels = RestaurantProductApi.filterByCategory(restaurantProductModels, "육류", "닭고기");
        //필터링 실행

        assertEquals(1, restaurantProductModels.size());
        assertEquals("닭고기", restaurantProductModels.get(0).categorySmall);
        //검증. 닭고기만 걸려야함.
    }

    @Test
    public void testFilteringByDistance() {
        restaurantProductModels = RestaurantProductApi.filterByDistance(restaurantProductModels, 0.0, 0.0, 300);
        //필터링 실행. 300미터 이내에 있는 것만 가져옴 300미터 이내에는 닭고기와 딸기만 있음.

        assertEquals(2, restaurantProductModels.size());
        String mustExistCategory[] = {"닭고기", "딸기"};
        assertContain(restaurantProductModels, mustExistCategory);
        //mustExistCategory의 내용물을 포함하는지 검증.

    }

    @Test
    public void testFilteringByPrice() {
        restaurantProductModels = RestaurantProductApi.filterByPrice(restaurantProductModels, 1500);
        //필터링 실행. 감자와 딸기만 1500원 이하임.

        assertEquals(2, restaurantProductModels.size());
        String mustExistCategory[] = {"감자", "딸기"};
        assertContain(restaurantProductModels, mustExistCategory);
        //mustExistCategory의 내용물을 포함하는지 검증.
    }

    @Test
    public void testFilteringByQuality() {
        restaurantProductModels = RestaurantProductApi.filterByQuality(restaurantProductModels, false, true, true);
        //필터링 실행. 감자는 품질이 하, 비허용(false)이므로 걸러져야함.

        assertEquals(3, restaurantProductModels.size());
        String mustExistCategory[] = {"닭고기", "딸기", "양파"};
        assertContain(restaurantProductModels, mustExistCategory);
        //mustExistCategory의 내용물을 포함하는지 검증.
    }

    @Test
    public void sortByFast() {
        RestaurantProductApi.sortByFast(restaurantProductModels);
        //소팅 실행. 딸기만 급처가 켜져있음.

        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
    }

    private void assertContain(ArrayList<RestaurantProductModel> restaurantProductModels, String[] actualExist) {
        boolean flag;
        for(int i =0; i < actualExist.length; i++) {
            flag = false;
            for(int j = 0; j < restaurantProductModels.size(); j++) {
                if(actualExist[i].equals(restaurantProductModels.get(j).categorySmall)) flag = true;
            }
            assertEquals(true, flag);
        }
    }

    private void assertSortByStock(ArrayList<RestaurantProductModel> restaurantProductModels) {
        //재고: 딸기(20) > 감자(10) > 양파(5) > 닭고기(1)
        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
        assertEquals("감자", restaurantProductModels.get(1).categorySmall);
        assertEquals("양파", restaurantProductModels.get(2).categorySmall);
        assertEquals("닭고기", restaurantProductModels.get(3).categorySmall);
    }

    private void assertSortByDistance(ArrayList<RestaurantProductModel> restaurantProductModels) {
        //거리: 양파(meter300*3) > 감자(meter300*2) > 닭고기(meter300/3) > 딸기(1)
        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
        assertEquals("닭고기", restaurantProductModels.get(1).categorySmall);
        assertEquals("감자",restaurantProductModels.get(2).categorySmall);
        assertEquals("양파",restaurantProductModels.get(3).categorySmall);
    }

    private void assertSortByPrice(ArrayList<RestaurantProductModel> restaurantProductModels) {
        //가격: 닭고기(6000) > 양파(3000) > 감자(1000) > 딸기(500)
        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
        assertEquals("감자", restaurantProductModels.get(1).categorySmall);
        assertEquals("양파", restaurantProductModels.get(2).categorySmall);
        assertEquals("닭고기", restaurantProductModels.get(3).categorySmall);
    }


    private ArrayList<RestaurantProductModel> getTestRestaurantProductModelList() {
        ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();
        restaurantProductModels.add(getDummyRestaurantProductModel("닭고기입니다.", 6000, 100, 1, "닭고기", 3));
        restaurantProductModels.add(getDummyRestaurantProductModel("양파양파.", 3000, 900, 5, "양파", 2));
        restaurantProductModels.add(getDummyRestaurantProductModel("성난감자.", 1000, 600, 10, "감자", 1));
        restaurantProductModels.add(getDummyRestaurantProductModel("딸기", 500, 10, 20, "딸기", 2));
        restaurantProductModels.get(3).fast = true;
        return restaurantProductModels;
    }
    /*
    테스트 목표:
    //가격: 닭고기(6000) > 양파(3000) > 감자(1000) > 딸기(500)
    //거리: 양파(meter300*3) > 감자(meter300*2) > 닭고기(meter300/3) > 딸기(10)
    //재고: 딸기(20) > 감자(10) > 양파(5) > 닭고기(1)
    //카테고리 필터링: 닭고기만 필터링
    //거리 필터링: 양파와 감자만 필터링
    //가격 필터링: 닭고기(6000), 양파(3000)는 필터링
    //품질 필터링: 감자(하)을 거르기
    */


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
