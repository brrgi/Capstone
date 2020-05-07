package com.example.msg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.Domain.RestaurantProductApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.fragment.AccountFragment;
import com.example.msg.fragment.ChatFragment;
import com.example.msg.fragment.HomeFragment;
import com.example.msg.fragment.ReservationFragment;
import com.example.msg.fragment.WriteFragment;
import com.example.msg.DatabaseModel.UserProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;  //하단 바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private ChatFragment chatFragment;
    private WriteFragment writeFragment;
    private ReservationFragment reservationFragment;
    private AccountFragment accountFragment;
    private Button backLogin;
    private ArrayList<UserProductModel> upm = UserProductApi.getProductList(5.0, 5.0, 500);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backLogin=(Button)findViewById(R.id.mainActivity_button_backLogin);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "로그인 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //Test Code
        //테스트 항목, 업데이트, 물량순 정렬, 재고순 정렬 위치 긁어오기.
        final ArrayList<RestaurantProductModel> restaurantProductModelArrayList1 = new ArrayList<RestaurantProductModel>();
        RestaurantProductApi.getProductList(5.0, 5.0, 100, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                backLogin.setEnabled(false);
                restaurantProductModelArrayList1.addAll(restaurantModelArrayList);
                RestaurantProductApi.sortByPrice(restaurantProductModelArrayList1);
                for(int i =0; i<restaurantProductModelArrayList1.size(); i++) {
                    Log.d("TestData", restaurantProductModelArrayList1.get(i).title);
                }
            }

            @Override
            public void onFail(ArrayList<RestaurantProductModel> restaurantProductModelArrayList) {

            }
        });

        //Test Code

        bottomNavigationView=findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        UserProductApi.sortByDistance(upm, 5.0, 5.0);
                        UserProductApi.getProduct(new UserProductApi.MyCallback() {
                            @Override
                            public void onCallback(UserProductModel userProductModel) {
                                Log.d("MyCallBack", userProductModel.title);
                            }
                        }, upm.get(0).uproduct_id);
                        try {

                        } catch(Exception e) {Log.d("Test", e.toString());}
                            for (int i = 0; i < upm.size(); i++) {
                                Log.d("Test", upm.get(i).title);
                            }
                        setFrag(0);
                        break;
                    case R.id.action_chat:
                        setFrag(1);
                        break;
                    case R.id.action_write:
                        setFrag(2);
                        break;
                    case R.id.action_reservation:
                        setFrag(3);
                        break;
                    case R.id.action_account:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
        homeFragment=new HomeFragment();
        chatFragment=new ChatFragment();
        writeFragment=new WriteFragment();
        reservationFragment=new ReservationFragment();
        accountFragment=new AccountFragment();
        setFrag(0); //첫 fragment 화면을 무엇으로 지정해줄 것인지 선택


    }

    //fragment 교체가 일어나는 실행문.
    private void setFrag(int n){
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        switch (n){
            case 0:


                ft.replace(R.id.mainactivity_framelayout,homeFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.mainactivity_framelayout,chatFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.mainactivity_framelayout,writeFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.mainactivity_framelayout,reservationFragment);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.mainactivity_framelayout,accountFragment);
                ft.commit();
                break;
//kyudong
        }
    }

    //BBBBBBBB
}
