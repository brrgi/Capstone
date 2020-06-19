package com.example.msg.saleFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.SaleApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.R;
import com.example.msg.RecyclerView.ResReviewsAdapter;

import java.util.ArrayList;

public class ResReviewsFragment extends Fragment {
    private View view;
    private static final String TAG = "ResReviewsFragment";
    private String res_id;

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //리사이클러뷰 Sales 전용 변수
    private RecyclerView.Adapter saleAdapter;
    private ArrayList<SaleModel> saleModelArrayList = new ArrayList<SaleModel>();

    private void initializeLayout(final Context context) {

        Bundle bundle=getArguments();
        if(bundle !=null) {
            res_id = bundle.getString("res_id");
        }

        //리사이클러뷰 관련 초기화
        recyclerView = view.findViewById(R.id.resReview_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);


        SaleApi.getSaleByResId(res_id, new SaleApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SaleModel> saleModels) {

                saleModelArrayList.clear();
                //saleModelArrayList.addAll(saleModels);
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

        saleAdapter=new ResReviewsAdapter(saleModelArrayList,context);
        recyclerView.setAdapter(saleAdapter);






    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resreview,container,false);

        initializeLayout(container.getContext());

        return view;
    }
}