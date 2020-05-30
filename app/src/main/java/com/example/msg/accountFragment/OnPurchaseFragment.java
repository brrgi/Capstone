package com.example.msg.accountFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ShareApi;
import com.example.msg.Api.UserApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;

import java.util.ArrayList;

public class OnPurchaseFragment extends Fragment {
    private View view;
    private static final String TAG = "OnPurchaseFragment";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();
    private ArrayList<UserProductModel> userProductModels = new ArrayList<UserProductModel>();

    private RecyclerView.Adapter resAdapter;
    private final ArrayList<RestaurantProductModel> restaurantProductModelArrayList = new ArrayList<RestaurantProductModel>();

    private Button btn_switch;



    private void initializeLayout(final Context context) {
        //리사이클러뷰 관련 초기화
        recyclerView = view.findViewById(R.id.onpurchase_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new com.example.msg.RecyclerView.UserProductsAdapter(userProductModelArrayList, context);
    }

    private void PurchaseUserHistory() {
        final String uid = AuthenticationApi.getCurrentUid();
        ShareApi.getShareByToId(uid, new ShareApi.MyListCallback() {
            @Override
            public void onSuccess(final ArrayList<ShareModel> shareModelArrayList) {
                for(int i=0;i<shareModelArrayList.size();i++) {
                    UserProductApi.getProduct((shareModelArrayList.get(i).uproduct_id), new UserProductApi.MyCallback() {
                        @Override
                        public void onSuccess(UserProductModel userProductModel) {
                            if(userProductModel.completed==0) {
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_on_purchase,container,false);
        //여기서 버튼 추가부터 시작
        PurchaseUserHistory();
        initializeLayout(container.getContext());
        return view;
    }

}
