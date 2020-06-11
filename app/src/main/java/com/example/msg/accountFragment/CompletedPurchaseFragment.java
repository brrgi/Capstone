package com.example.msg.accountFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.SaleApi;
import com.example.msg.Api.ShareApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;
import com.example.msg.RecyclerView.ResProductsAdapter;
import com.example.msg.RecyclerView.UserProductsAdapter;

import java.util.ArrayList;

public class CompletedPurchaseFragment extends Fragment {
    private View view;
    private static final String TAG = "CompletedPurchaseFragment";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private final ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();
    private final ArrayList<UserProductModel> userProductModels = new ArrayList<UserProductModel>();

    private RecyclerView.Adapter resAdapter;
    private final ArrayList<RestaurantProductModel> restaurantProductModelArrayList = new ArrayList<RestaurantProductModel>();
    private final ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<RestaurantProductModel>();

    final String uid = AuthenticationApi.getCurrentUid();

    private Button btn_switch;

    private boolean isUserProduct = true;


    //refreshLayout 변수
    private SwipeRefreshLayout refreshLayout;

    private void initializeLayout(final Context context) {
        //refreshLayout 초기화
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.completedpurchasefragment_swipeLayout);
        //리사이클러뷰 관련 초기화
        recyclerView = view.findViewById(R.id.completedpurchase_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);


        resAdapter = new ResProductsAdapter(restaurantProductModelArrayList, context);
        userAdapter = new UserProductsAdapter(userProductModelArrayList, context);

        btn_switch = view.findViewById(R.id.completedpurchasefragment_switch_button);

        //    userAdapter = new com.example.msg.RecyclerView.UserProductsAdapter(userProductModelArrayList, context);
        if(isUserProduct) ComPurchaseUserHistory();
        else ComPurchaseResHistory();

        //refreshLayout false
        refreshLayout.setRefreshing(false);
    }

    private void ComPurchaseUserHistory() {
        ShareApi.getShareByToId(uid, new ShareApi.MyListCallback() {
            @Override
            public void onSuccess(final ArrayList<ShareModel> shareModelArrayList) {
                restaurantProductModelArrayList.clear();
                restaurantProductModels.clear();
                for(int i=0;i<shareModelArrayList.size();i++) {
                    UserProductApi.getProduct((shareModelArrayList.get(i).uproduct_id), new UserProductApi.MyCallback() {
                        @Override
                        public void onSuccess(UserProductModel userProductModel) {
                            if(userProductModel.completed==1) {
                                userProductModels.add(userProductModel);
                            }
                            recyclerView.setAdapter(userAdapter);
                            userProductModelArrayList.clear();
                            Log.d("Purchase 2: ",Integer.toString(userProductModels.size()));

                            userProductModelArrayList.addAll(userProductModels);
                            userAdapter.notifyDataSetChanged();
                            //}
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

    private void ComPurchaseResHistory() {
        SaleApi.getSaleByUserId(uid, new SaleApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SaleModel> saleModels) {
                userProductModelArrayList.clear();
                userProductModels.clear();
                for(int i=0;i<saleModels.size();i++) {
                    RestaurantProductApi.getProduct((saleModels.get(i).product_id), new RestaurantProductApi.MyCallback() {
                        @Override
                        public void onSuccess(RestaurantProductModel restaurantProductModel) {
                            if(restaurantProductModel.completed==1) {
                                restaurantProductModels.add(restaurantProductModel);
                            }
                            recyclerView.setAdapter(resAdapter);
                            restaurantProductModelArrayList.clear();
                            Log.d("Purchase 2: ",Integer.toString(userProductModels.size()));

                            restaurantProductModelArrayList.addAll(restaurantProductModels);
                            resAdapter.notifyDataSetChanged();
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_completed_purchase,container,false);
        initializeLayout(getContext());

        //refreshLayout로 새로고침했을 때
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userProductModelArrayList.clear();
                userProductModels.clear();
                restaurantProductModelArrayList.clear();
                restaurantProductModels.clear();
                initializeLayout(container.getContext());
            }
        });
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {


        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUserProduct) {
                    userProductModelArrayList.clear();
                    userProductModels.clear();
                    ComPurchaseResHistory();
                    isUserProduct=false;
                }
                else  {
                    restaurantProductModelArrayList.clear();
                    restaurantProductModels.clear();
                    isUserProduct=true;
                    ComPurchaseUserHistory();
                }
            }
        });
    }

}