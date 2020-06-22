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
    private ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.msg", appContext.getPackageName());

       //common Setup Fixture
       restaurantProductModels = new ArrayList<>();
       restaurantProductModels.addAll(getTestRestaurantProductModelList());
    }

    @After
    public void commonCleaningUpFixture() {
        //common Cleaning up fixture
    }

    @Test
    public void testSortingByDistance() {
        //Exercise SUT
        RestaurantProductApi.sortByDistance(restaurantProductModels, 0, 0);

        //Verify OutCome
        assertSortByDistance("거리순으로 정렬됐는지 검증.",restaurantProductModels);

    }

    @Test
    public void testSortingByPrice() {
        //Exercise SUT
        RestaurantProductApi.sortByPrice(restaurantProductModels);

        //Verify OutCome
        assertSortByPrice("가격순으로 정렬됐는지 검증.",restaurantProductModels);
    }

    @Test
    public void testSortingByStock() {
        //Exercise SUT
        RestaurantProductApi.sortByStock(restaurantProductModels);
        //Verify OutCome
        assertSortByStock("재고순으로 정렬됐는지 검증", restaurantProductModels);
    }



    @Test
    public void testFilteringByCategory() {
        //Exercise SUT
        restaurantProductModels = RestaurantProductApi.filterByCategory(restaurantProductModels, "닭고기");

        //Verify OutCome
        assertEquals("닭고기만 걸러지므로 모델은 단 한개다.",1, restaurantProductModels.size());
        assertEquals("닭고기만 걸러지므로 첫 번째 원소가 닭고기다.","닭고기", restaurantProductModels.get(0).categorySmall);
    }

    @Test
    public void testFilteringByDistance() {
        //Exercise SUT: 반경 300m로 필터링 실행.
        restaurantProductModels = RestaurantProductApi.filterByDistance(restaurantProductModels, 0.0, 0.0, 300);

        //Verify OutCome
        String mustExistCategory[] = {"닭고기", "딸기"};
        assertEquals("반경 300m 이내에는 물품이 닭고기와 딸기 밖에 없다.",2, restaurantProductModels.size());
        assertContain("위 물품이 포함돼있는지 검증.",restaurantProductModels, mustExistCategory);

    }

    @Test
    public void testFilteringByPrice() {
        //Exercise SUT: 가격 1500이하만 필터링.
        restaurantProductModels = RestaurantProductApi.filterByPrice(restaurantProductModels, 1500);

        //Verify Outcome
        assertEquals("1500원 이하는 감자와 딸기만 있다.",2, restaurantProductModels.size());
        String mustExistCategory[] = {"감자", "딸기"};
        assertContain("위 물품을 포함하는지 확인한다.",restaurantProductModels, mustExistCategory);
    }

    @Test
    public void testFilteringByQuality() {
        //Exercise SUT: 품질 '하'만 걸러내기.
        restaurantProductModels = RestaurantProductApi.filterByQuality(restaurantProductModels, false, true, true);

        //Verify Outcome
        assertEquals("4가지 더미 중에 감자만 품질 '하'",3, restaurantProductModels.size());
        String mustExistCategory[] = {"닭고기", "딸기", "양파"};
        assertContain("위 물품을 포함하는지 확인한다.",restaurantProductModels, mustExistCategory);
    }

    @Test
    public void sortByFast() {
        //Exercise SUT: 딸기만 급처가 켜져있음.
        RestaurantProductApi.sortByFast(restaurantProductModels);

        assertEquals("급처가 켜진 딸기가 최상단에 와야한다.","딸기", restaurantProductModels.get(0).categorySmall);
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

    private void assertSortByStock(String msg, ArrayList<RestaurantProductModel> restaurantProductModels) {
        //재고: 딸기(20) > 감자(10) > 양파(5) > 닭고기(1)
        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
        assertEquals("감자", restaurantProductModels.get(1).categorySmall);
        assertEquals("양파", restaurantProductModels.get(2).categorySmall);
        assertEquals("닭고기", restaurantProductModels.get(3).categorySmall);
    }

    private void assertSortByDistance(String msg, ArrayList<RestaurantProductModel> restaurantProductModels) {
        //거리: 양파(meter300*3) > 감자(meter300*2) > 닭고기(meter300/3) > 딸기(1)
        assertEquals("딸기", restaurantProductModels.get(0).categorySmall);
        assertEquals("닭고기", restaurantProductModels.get(1).categorySmall);
        assertEquals("감자",restaurantProductModels.get(2).categorySmall);
        assertEquals("양파",restaurantProductModels.get(3).categorySmall);
    }

    private void assertSortByPrice(String msg, ArrayList<RestaurantProductModel> restaurantProductModels) {
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
