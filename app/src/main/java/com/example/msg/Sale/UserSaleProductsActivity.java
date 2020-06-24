package com.example.msg.Sale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;

import java.util.ArrayList;

public class UserSaleProductsActivity extends AppCompatActivity {
    private static final String TAG = "CompletedSaleResFagment";

    private String reported_user_id;

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private final ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();

    private void SalesHistory() {
        UserProductApi.getProductListById(reported_user_id, -1, new UserProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<UserProductModel> userProductModels) {
                recyclerView.setAdapter(userAdapter);
                userProductModelArrayList.clear();
                userProductModelArrayList.addAll(userProductModels);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersaleproducts);

        Intent intent=getIntent();
        reported_user_id=intent.getStringExtra("reported_user_id");

        //리사이클러뷰 관련 초기화
        recyclerView = findViewById(R.id.usersaleproducts_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new com.example.msg.RecyclerView.UserProductsAdapter(userProductModelArrayList, this);

        SalesHistory();
    }
}
