package com.example.msg.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.Domain.RestaurantProductApi;
import com.example.msg.R;
import com.example.msg.recyclerView.RestaurantModel;
import com.example.msg.recyclerView.ProductsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private View view;
    private static final String TAG = "HomeFragment";


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RestaurantModel> arrayList;
    private ArrayList<RestaurantProductModel> arrayListTemp = new ArrayList<RestaurantProductModel>();
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    ArrayList spinnerList = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter arrayAdapter;
    private ImageButton searchButton;
    private EditText searchText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Context context = view.getContext();
        recyclerView = view.findViewById(R.id.home_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        //layoutManager = new LinearLayoutManager(getActivity()); //fragment일 때 LinearLayoutManager 인자
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<RestaurantProductModel> arrayList = new ArrayList<RestaurantProductModel>();


        RestaurantProductApi.getProductList(5.0, 5.0, 100, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                arrayList.addAll(restaurantModelArrayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        adapter = new ProductsAdapter(arrayList, context);
        recyclerView.setAdapter(adapter);

        spinnerList.add("거리 순 정렬");
        spinnerList.add("가격 순 정렬");
        spinnerList.add("재고 순 정렬");

        spinner = (Spinner) view.findViewById(R.id.home_spinner_sort);

        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setTextColor(Color.BLACK);
                switch(position) {
                    case 0:
                        RestaurantProductApi.sortByDistance(arrayList, 5.0, 5.0);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        RestaurantProductApi.sortByPrice(arrayList);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        RestaurantProductApi.sortByStock(arrayList);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchButton = (ImageButton) view.findViewById(R.id.home_button_search);
        searchText = (EditText)view.findViewById(R.id.home_editText_search);
        try {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchText.getText() != null) {
                        arrayListTemp.clear();
                        arrayListTemp = RestaurantProductApi.filterByKeyWord(arrayList, searchText.getText().toString());
                        arrayList.clear();
                        arrayList.addAll(arrayListTemp);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }catch(Exception e) {Log.d("1234", e.toString());}

    }

}
//minor test2
