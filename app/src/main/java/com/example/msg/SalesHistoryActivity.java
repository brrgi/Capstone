package com.example.msg;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.AuthenticationApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.recyclerView.UserProductsAdapter;

import java.util.ArrayList;

public class SalesHistoryActivity extends AppCompatActivity {

    private static final String TAG = "SalesHistoryActivity";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private final ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();
    private ArrayList<UserProductModel> filteredUserModels = new ArrayList<UserProductModel>();

    private void initialize() {
        //리사이클러뷰 관련 초기화
        recyclerView = findViewById(R.id.home_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new UserProductsAdapter(filteredUserModels, this);
    }

    private void SalesHistory() {
        UserProductApi.getProductListById(AuthenticationApi.getCurrentUid(), 0, new UserProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<UserProductModel> userProductModels) {
                recyclerView.setAdapter(userAdapter);
                userProductModelArrayList.clear();
                userProductModelArrayList.addAll(userProductModels);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                Log.d("UserProduct Test", e.toString());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);
        initialize();


    }
}
