package com.example.msg.Subscription;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.SubscriptionApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.SubscriptionModel;
import com.example.msg.R;
import com.example.msg.RecyclerView.ReserveAdapter;
import com.example.msg.RecyclerView.SubscriptionsAdapter;
import com.example.msg.Sale.SalesHistoryActivity;
import com.example.msg.accountFragment.CompletedSaleFagment;
import com.example.msg.accountFragment.OnSaleFagment;
import com.example.msg.accountFragment.TradingFagment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView.Adapter subscriptionsAdapter;
    private ArrayList<RestaurantModel> restaurantModels = new ArrayList<RestaurantModel>();
    private ArrayList<RestaurantModel> tempRestaurantModels = new ArrayList<RestaurantModel>();

    private SwipeRefreshLayout refreshLayout;

    private void initializeLayout(final Context context) {
        refreshLayout=findViewById(R.id.subscription_swipeLayout);

        recyclerView =findViewById(R.id.subscription_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        subscriptionsAdapter = new SubscriptionsAdapter(restaurantModels,context);
        SubscriptionList();
        //refreshLayout false
        refreshLayout.setRefreshing(false);
    }

    private void SubscriptionList() {
        restaurantModels.clear();
        SubscriptionApi.getSubscriptionListByUserId(AuthenticationApi.getCurrentUid(), new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                for(int i=0;i<subscriptionModelArrayList.size();i++) {
                    RestaurantApi.getUserById((subscriptionModelArrayList.get(i).res_id), new RestaurantApi.MyCallback() {
                        @Override
                        public void onSuccess(RestaurantModel restaurantModel) {
                            tempRestaurantModels.add(restaurantModel);
                            recyclerView.setAdapter(subscriptionsAdapter);
                            restaurantModels.clear();
                            restaurantModels.addAll(tempRestaurantModels);
                            subscriptionsAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFail(int errorCode, Exception e) {

                        }
                    });
                }

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        initializeLayout(getApplicationContext());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tempRestaurantModels.clear();
                restaurantModels.clear();
                initializeLayout(getApplicationContext());
            }
        });

    }

}
