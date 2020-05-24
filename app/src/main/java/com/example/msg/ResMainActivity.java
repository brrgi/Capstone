
package com.example.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.msg.fragment.ResAccountBookFragment;
import com.example.msg.fragment.ResAccountFragment;
import com.example.msg.fragment.ResChatFragment;
import com.example.msg.fragment.ResWriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;  //하단 바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ResChatFragment chatFragment;
    private ResWriteFragment writeFragment;
    private ResAccountFragment accountFragment;
    private ResAccountBookFragment accountBookFragment;

    private Button backLogin;

    private Button sale_btn;
    private Button map;
    private Button address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resmain);
        bottomNavigationView=findViewById(R.id.resmainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_chat:
                        setFrag(0);
                        break;
                    case R.id.action_write:
                        Intent intent = new Intent(ResMainActivity.this, ProductUploadActivity.class);
                        startActivity(intent);
                        setFrag(1);
                        break;
                    case R.id.action_account:
                        setFrag(2);
                        break;
                    case R.id.action_accountbook:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });


        chatFragment=new ResChatFragment();
        writeFragment=new ResWriteFragment();
        accountFragment=new ResAccountFragment();
        accountBookFragment=new ResAccountBookFragment();

        setFrag(0); //첫 fragment 화면을 무엇으로 지정해줄 것인지 선택


    }

    //fragment 교체가 일어나는 실행문.
    private void setFrag(int n){
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.resmainactivity_framelayout,chatFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.resmainactivity_framelayout,writeFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.resmainactivity_framelayout,accountFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.resmainactivity_framelayout,accountBookFragment);
                ft.commit();
                break;
        }
    }
}
