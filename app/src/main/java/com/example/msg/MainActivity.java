package com.example.msg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.msg.ChatRoom.ChatRoomActivity;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.UserApi;
import com.example.msg.Sale.PopupSearchActivity;
import com.example.msg.Upload.ProductRestUploadActivity;
import com.example.msg.Upload.ProductUploadActivity;
import com.example.msg.UserFragment.AccountFragment;
import com.example.msg.UserFragment.ChatFragment;
import com.example.msg.UserFragment.HomeFragment;
import com.example.msg.UserFragment.ReservationFragment;
import com.example.msg.UserFragment.WriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;

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

    private Button sale_btn;
    private Button map;
    private Button address;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppKeyHash();      //키해시 구하기
        /*
        dummy code in here
        UserModel userModel = new UserModel();
        userModel.user_id = AuthenticationApi.getCurrentUid();
        userModel.age = 22;
        userModel.ban_count = 0;
        userModel.email = "test2@gmail.com";
        userModel.is_male = true;
        userModel.mileage = 50;
        userModel.user_address = "";
        userModel.user_grade = 0;
        userModel.user_name = "한규동";
        userModel.user_phone = "010-0000-0000";
        UserApi.postUser(userModel, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
         */
        UserApi.getUserById(AuthenticationApi.getCurrentUid(), new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                Log.d("1234", userModel.user_name);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        //mainActivity_address
//        address=(Button)findViewById(R.id.mainActivity_address);
//        address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, DaumWebViewActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "주소로 갑니다.", Toast.LENGTH_LONG).show();
////                finish();
//            }
//        });

//        map=(Button)findViewById(R.id.mainActivity_button_map);
//        map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "지도로 갑니다.", Toast.LENGTH_LONG).show();
////                finish();
//            }
//        });

//        backLogin=(Button)findViewById(R.id.mainActivity_button_backLogin);
//        backLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "로그인 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
////                finish();
//            }
//        });
//
//        sale_btn = (Button)findViewById(R.id.sale_btn);
//        sale_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
//                startActivity(intent);
////                finish();
//            }
//        });

        bottomNavigationView=findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                    Intent intent = new Intent(this, PopupSearchActivity.class);
                    intent.putExtra("data", "Test Popup");
                    startActivityForResult(intent, 156);
                return true;
            case R.id.action_filter:
                return true;
            default:
                return super.onOptionsItemSelected((item));
        }
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 156) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    //BBBBBBBB
}
