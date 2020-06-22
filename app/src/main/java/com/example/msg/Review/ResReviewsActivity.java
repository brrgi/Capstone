package com.example.msg.Review;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.SaleApi;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.R;
import com.example.msg.RecyclerView.ResReviewsAdapter;

import java.util.ArrayList;

public class ResReviewsActivity extends AppCompatActivity {
    private static final String TAG = "ResReviewsActivity";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 Sales 전용 변수
    private RecyclerView.Adapter saleAdapter;
    private ArrayList<SaleModel> saleModelArrayList = new ArrayList<SaleModel>();

    private void initializeLayout(final Context context) {


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_resreview);

        //리사이클러뷰 관련 초기화
        recyclerView = findViewById(R.id.resReview_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        SaleApi.getSaleByResId(AuthenticationApi.getCurrentUid(), new SaleApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SaleModel> saleModels) {

                saleModelArrayList.clear();
                for(int index=0;index<saleModels.size();index++){
                    if(saleModels.get(index).review!=null)
                        saleModelArrayList.add(saleModels.get(index));
                }
                saleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        saleAdapter=new ResReviewsAdapter(saleModelArrayList,this);
        recyclerView.setAdapter(saleAdapter);

    }
}
