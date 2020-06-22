package com.example.msg.UserFragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.msg.Api.UserApi;
import com.example.msg.Filter.FilterModel;
import com.example.msg.DatabaseModel.FoodModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.Filter.FilterSelectActivity;
import com.example.msg.R;
import com.example.msg.RecyclerView.ResProductsAdapter;
import com.example.msg.RecyclerView.UserProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment  {
    private View view;
    private static final String TAG = "HomeFragment";

    //액티비티 코드
    private final int FILTER_CODE = 100;

    //레이아웃 요소들
    private ArrayList spinnerList = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private ImageButton searchButton;
    private EditText searchText;
    private Button dummy;

    private Button address1;
    private Button address2;
    private double altitude;
    private TextView address;
    private ImageButton filter;

    private SwipeRefreshLayout refreshLayout;

    //HomeFragment에서 사용되는 수치값
    private double defaultLongitude = 0;
    private double defaultLatitude = 0;
    private int range = 10000000;
    private String dong="";
    static int state = -1;


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



    private void getAddress(String uid){
        UserApi.getUserById(uid, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                defaultLatitude=userModel.latitude;
                defaultLongitude=userModel.longitude;
                //Toast.makeText(getActivity(), defaultLatitude+" "+defaultLongitude, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }
    //주소얻기

    private void initializeLayout(final Context context) {
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


        searchButton = (ImageButton) view.findViewById(R.id.home_button_search);
        searchText = (EditText) view.findViewById(R.id.home_editText_search);
        filter = (ImageButton)view.findViewById(R.id.home_btn_sort);
        dummy = (Button) view.findViewById(R.id.home_button_dummy);
        address1=(Button)view.findViewById(R.id.home_button_address1);
        address2=(Button)view.findViewById(R.id.home_button_address2);
        address=(TextView)view.findViewById(R.id.home_TextView_dong);
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.home_swipeLayout);
        if(isShowingUserProduct) refreshItemOfUserProducts();
        else refreshItemOfResProducts();

        refreshLayout.setRefreshing(false);
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
    /*
    입력:
    출력:
    동작:
     */


    private void sortItemOfResProducts(String sortBy) {
        switch(sortBy) {
            case "distance":
                RestaurantProductApi.sortByDistance(restaurantProductModels, defaultLatitude, defaultLongitude);
                break;
            case "price":
                RestaurantProductApi.sortByPrice(restaurantProductModels);
                break;
            case "stock":
                RestaurantProductApi.sortByStock(restaurantProductModels);
                break;
            default:
                Log.d("HomeFragmentError", "sortBy value is not proper");
                break;
        }
        filteredResModels.clear();
        filteredResModels.addAll(restaurantProductModels);
        resAdapter.notifyDataSetChanged();

    }


    private void refreshItemOfResProducts() {
        RestaurantProductApi.getProductList(defaultLatitude, defaultLongitude, range, new RestaurantProductApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList) {
                recyclerView.setAdapter(resAdapter);
                restaurantProductModels.clear();
                restaurantProductModels.addAll(restaurantModelArrayList);
                RestaurantProductApi.sortByFast(restaurantModelArrayList);
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
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        initializeLayout(context);

        //swipelayout관련 추가
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeLayout(context);
            }
        });



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        getAddress(uid);

//    UserApi
        getLocation(defaultLatitude,defaultLongitude);
        //스피너 선택

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterSelectActivity.class);
                intent.putExtra("isShowingUserProduct", isShowingUserProduct);
                startActivityForResult(intent, FILTER_CODE);
            }
        });
        //검색 버튼

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText() != null) {
                    if(isShowingUserProduct) {
                        filteredUserModels.clear();
                        UserProductApi.keywordSend(searchText.getText().toString(), new UserProductApi.MyFilterCallback() {
                            @Override
                            public void onSuccess(FoodModel foodModel) {
                                filteredUserModels.addAll(UserProductApi.filterByKeyWord(userProductModelArrayList,foodModel));
                                filteredUserModels.addAll(UserProductApi.filterByKeyWord2(userProductModelArrayList,searchText.getText().toString()));
                                userAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(int errorCode, Exception e) {

                            }
                        });
                    }
                    else {
                        filteredResModels.clear();
                        RestaurantProductApi.keywordSend(searchText.getText().toString(), new RestaurantProductApi.MyFilterCallback() {
                            @Override
                            public void onSuccess(FoodModel foodModel) {
                                filteredResModels.addAll(RestaurantProductApi.filterByKeyWord(restaurantProductModels, foodModel));
                                resAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(int errorCode, Exception e) {

                            }
                        });
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
                    dummy.setText("식당 상품");
                }
                else {
                    isShowingUserProduct = true;
                    refreshItemOfUserProducts();
                    dummy.setText("공유 상품");
                }
            }
        });

        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                UserApi.getUserById(uid, new UserApi.MyCallback() {
                    @Override
                    public void onSuccess(UserModel userModel) {

                        defaultLatitude=userModel.latitude;
                        defaultLongitude=userModel.longitude;
                        getLocation(defaultLatitude,defaultLongitude);

                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });

            }
        });

        address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );

                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    defaultLongitude = location.getLongitude();
                    defaultLatitude = location.getLatitude();
                    altitude = location.getAltitude();

                    getLocation(defaultLatitude,defaultLongitude);
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }


            }
        });




    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            defaultLongitude = location.getLongitude();
            defaultLatitude = location.getLatitude();
            altitude = location.getAltitude();

//                txtResult.setText("위치정보 : " + provider + "\n" +
//                        "위도 : " + longitude + "\n" +
//                        "경도 : " + latitude + "\n" +
//                        "고도  : " + altitude);

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public void getLocation(double lat, double lng){
        String addressString = null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.KOREAN);
        Log.d("GOS", lat+" "+lng);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 10);
            for (int i=0; i<addresses.size(); i++) {
                if(addresses.get(i).getThoroughfare() != null ) {
                    dong = addresses.get(i).getThoroughfare();
                    address.setText(dong);
                }
//                    Log.d("GOS", addresses.get(i).getThoroughfare());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_CODE && resultCode ==  getActivity().RESULT_OK) {
            FilterModel filterModel = (FilterModel)data.getSerializableExtra("Object");
            sortItemOfResProducts(filterModel.getFilterType());
            if(isShowingUserProduct) {
                filteringUserProduct(filterModel);
            } else {
                filteringResProduct(filterModel);
            }
        }
    }

   public void filteringUserProduct(FilterModel filterModel) {

   }

   public void filteringResProduct(FilterModel filterModel) {
        refreshItemOfResProducts();

        ArrayList<RestaurantProductModel> temps = new ArrayList<>();
        temps.addAll(restaurantProductModels);

        if(filterModel.getCategory() != null) temps =  RestaurantProductApi.filterByCategory(temps, filterModel.getCategory());
        temps = RestaurantProductApi.filterByDistance(temps, defaultLatitude, defaultLongitude, filterModel.getRange());
        temps = RestaurantProductApi.filterByPrice(temps, filterModel.getPrice());
        temps = RestaurantProductApi.filterByQuality(temps, filterModel.isSearchLowQuality(), filterModel.isSearchMidQuality(), filterModel.isSearchHighQuality());

        filteredResModels.clear();
        filteredResModels.addAll(temps);
        resAdapter.notifyDataSetChanged();

   }

}
