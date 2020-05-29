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

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;

import java.util.ArrayList;

public class OnSaleFagment extends Fragment {
    private View view;
    private static final String TAG = "OnSaleFagment";

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private final ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();

    private void initializeLayout(final Context context) {
        //리사이클러뷰 관련 초기화
        recyclerView = view.findViewById(R.id.onsale_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new com.example.msg.RecyclerView.UserProductsAdapter(userProductModelArrayList, context);
    }

    private void SalesHistory() {
        UserProductApi.getProductListById(AuthenticationApi.getCurrentUid(), -1, new UserProductApi.MyListCallback() {
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_onsale,container,false);
        SalesHistory();
        initializeLayout(container.getContext());
        return view;
    }
}
