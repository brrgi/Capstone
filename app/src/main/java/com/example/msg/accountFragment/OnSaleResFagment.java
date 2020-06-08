package com.example.msg.accountFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.R;

import java.util.ArrayList;

public class OnSaleResFagment extends Fragment {
    private View view;
    private static final String TAG = "OnSaleResFagment";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 RestaurantProducts 전용 변수
    private RecyclerView.Adapter resAdapter;
    private final ArrayList<RestaurantProductModel> restaurantProductModelArrayList = new ArrayList<RestaurantProductModel>();

    //refreshLayout 변수
    private SwipeRefreshLayout refreshLayout;

    private void initializeLayout(final Context context) {
        //refreshLayout 초기화
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.onsale_res_swipeLayout);

        //리사이클러뷰 관련 초기화
        recyclerView = view.findViewById(R.id.onsale_res_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        resAdapter = new com.example.msg.RecyclerView.ResProductsAdapter(restaurantProductModelArrayList, context);

        SalesHistory();
        //refreshLayout false
        refreshLayout.setRefreshing(false);
    }

    private void SalesHistory() {
        RestaurantProductApi.getProductListById(AuthenticationApi.getCurrentUid(), -1, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                recyclerView.setAdapter(resAdapter);
                restaurantProductModelArrayList.clear();
                restaurantProductModelArrayList.addAll(restaurantModelArrayList);
                resAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_res_onsale,container,false);

        initializeLayout(container.getContext());
        //refreshLayout로 새로고침했을 때
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeLayout(container.getContext());
            }
        });
        return view;
    }
}
