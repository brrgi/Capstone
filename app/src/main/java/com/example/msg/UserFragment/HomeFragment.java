package com.example.msg.UserFragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.R;
import com.example.msg.RecyclerView.ResProductsAdapter;
import com.example.msg.RecyclerView.UserProductsAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private View view;
    private static final String TAG = "HomeFragment";

    //레이아웃 요소들
    private ArrayList spinnerList = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private ImageButton searchButton;
    private EditText searchText;
    private Button dummy;


    //HomeFragment에서 사용되는 수치값
    private double defaultLongitude = 5.0;
    private double defaultLatitude = 5.0;
    private double range = 500;

    //리사이클러뷰 공통 변수
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    //리사이클러뷰 RestaurantProducts 전용 변수
    private RecyclerView.Adapter resAdapter;
    private  ArrayList<RestaurantProductModel> filteredResModels = new ArrayList<RestaurantProductModel>();
    private final ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<RestaurantProductModel>();

    //리사이클러뷰 UserProducts 전용 변수.
    private RecyclerView.Adapter userAdapter;
    private final ArrayList<UserProductModel> userProductModelArrayList = new ArrayList<UserProductModel>();
    private ArrayList<UserProductModel> filteredUserModels = new ArrayList<UserProductModel>();

    //리사이클러뷰 선택에 사용되는 변수.
    private boolean isShowingUserProduct = true;



    private void initialize(final Context context) {
        //리사이클러뷰 관련 초기화
        //layoutManager = new LinearLayoutManager(getActivity()); //fragment일 때 LinearLayoutManager 인자
        recyclerView = view.findViewById(R.id.home_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        resAdapter = new ResProductsAdapter(filteredResModels, context);
        userAdapter = new UserProductsAdapter(filteredUserModels, context);

        //일반 레이아웃 관련 초기화.
        spinnerList.add("거리 순 정렬");
        spinnerList.add("가격 순 정렬");
        spinnerList.add("재고 순 정렬");

        spinner = (Spinner) view.findViewById(R.id.home_spinner_sort);
        searchButton = (ImageButton) view.findViewById(R.id.home_button_search);
        searchText = (EditText) view.findViewById(R.id.home_editText_search);
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(spinnerAdapter);
        dummy = (Button) view.findViewById(R.id.home_button_dummy);
    }
    //해당 프래그먼트에서 사용되는 레이아웃 등을 초기화 하는 함수입니다.

    private void refreshItemOfUserProducts() {
        UserProductApi.getProductList(defaultLatitude, defaultLongitude, range, new UserProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<UserProductModel> userProductModels) {
                recyclerView.setAdapter(userAdapter);
                userProductModelArrayList.clear();
                userProductModelArrayList.addAll(userProductModels);
                filteredUserModels.clear();
                filteredUserModels.addAll(userProductModels);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                Log.d("UserProduct Test", e.toString());
            }
        });
    }

    private void refreshItemOfResProducts() {
        RestaurantProductApi.getProductList(defaultLatitude, defaultLongitude, range, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                recyclerView.setAdapter(resAdapter);
                restaurantProductModels.clear();
                restaurantProductModels.addAll(restaurantModelArrayList);
                filteredResModels.clear();
                filteredResModels.addAll(restaurantProductModels);
                resAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
        //TODO: curLatitude와 curLongitude를 지도에서 받아온 값으로 바꾸고, range 또한 받는 인터페이스가 필요함.


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Context context = view.getContext();
        initialize(context);

        if(isShowingUserProduct) refreshItemOfUserProducts();
        else refreshItemOfResProducts();

        //스피너 선택
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                        //거리순 정렬
                        RestaurantProductApi.sortByDistance(filteredResModels, 5.0, 5.0);
                        resAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        //가격순 정렬
                        RestaurantProductApi.sortByPrice(filteredResModels);
                        resAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //재고순 정렬
                        RestaurantProductApi.sortByStock(filteredResModels);
                        resAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //검색 버튼
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText() != null) {
                    if(isShowingUserProduct) {
                        filteredUserModels.clear();
                        filteredUserModels.addAll(UserProductApi.filterByKeyWord(userProductModelArrayList, searchText.getText().toString()));
                    }
                    else {
                        filteredResModels.clear();
                        filteredResModels.addAll(RestaurantProductApi.filterByKeyWord(restaurantProductModels, searchText.getText().toString()));
                        resAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
        //키워드 검색


        //더미 버튼
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowingUserProduct) {
                    isShowingUserProduct = false;
                    refreshItemOfResProducts();

                }
                else {
                    isShowingUserProduct = true;
                    refreshItemOfUserProducts();
                }
            }
        });


    }




}
