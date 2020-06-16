package com.example.msg.Sale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.SaleApi;
import com.example.msg.Api.ShareApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.SubscriptionModel;

import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.SubscriptionApi;

import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Map.MapActivity;
import com.example.msg.R;
import com.example.msg.RatingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SaleActivity extends AppCompatActivity {

    private int stuck = 10;

    private Button btn_chat;
    private Button btn_buy;
    private TextView txt_title;
    private TextView txt_category;
    private TextView txt_quantity;
    private TextView txt_quality;
    private TextView txt_expireDate;
    private TextView txt_description;
    private TextView txt_salesman;
    private TextView txt_address;
    private TextView txt_rating;
    private TextView txt_cost;
    private ImageView image_product;
    private Button btn_subscription;
    private Button love;
    private Button share;
    private final SubscriptionModel subscriptionModel = new SubscriptionModel();
    static int state = -1;
    private Button btn_evaluate;
    private RatingBar rating;
    private Button dummy;
    String r_sub = "";
    String r_name = "";


    private void processSale(final RestaurantProductModel restaurantProductModel) {
        SaleModel saleModel = new SaleModel();
        saleModel.res_id = restaurantProductModel.res_id;
        saleModel.user_id = AuthenticationApi.getCurrentUid();
        saleModel.product_id = restaurantProductModel.rproduct_id;
        restaurantProductModel.completed = 0;

        SaleApi.postSale(saleModel, new SaleApi.MyCallback() {
            @Override
            public void onSuccess(SaleModel saleModel) {
                RestaurantProductApi.updateProduct(restaurantProductModel, new RestaurantProductApi.MyCallback() {
                    @Override
                    public void onSuccess(RestaurantProductModel restaurantProductModel) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(restaurantProductModel.title);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

    private void getSubscribeCheck(RestaurantProductModel restaurantProductModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("subs3","구독");

        String uid = user.getUid();
        Log.d("uid1234", uid);

        r_sub = restaurantProductModel.res_id;
        SubscriptionApi.getSubscriptionListByUserId(uid, new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                for(int i =0;i < subscriptionModelArrayList.size();i++) {
                    if((subscriptionModelArrayList.get(i).res_id).equals(r_sub))
                    {
                        Log.d("subfailnew", "if state start");
                        subscriptionModel.user_id = subscriptionModelArrayList.get(i).user_id;
                        subscriptionModel.res_id = subscriptionModelArrayList.get(i).res_id;
                        subscriptionModel.subs_id = subscriptionModelArrayList.get(i).subs_id;

                        state = 1;


                    }
                    else {
                        Log.d("subfailnew", "else state");
                    }
                    Log.d("subfailnew", "for state end");
                }

                if(state == 1) btn_subscription.setText("구독 해지");
                else btn_subscription.setText("구독");
            }
            @Override
            public void onFail(int errorCode, Exception e) {
                Log.d("subfail", Integer.toString(errorCode));
            }
        });
    }

    private void subscribeClick(final RestaurantProductModel restaurantProductModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        if(state==-1) {
            subscriptionModel.res_id = restaurantProductModel.res_id;
            subscriptionModel.user_id = uid;
            state=1;
            btn_subscription.setText("구독 해지");
            SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
                @Override
                public void onSuccess(SubscriptionModel subscriptionModel) {
                    Toast.makeText(getApplicationContext(), "구독 완료!", Toast.LENGTH_LONG).show();
                    FirebaseMessaging.getInstance().subscribeToTopic(restaurantProductModel.res_id)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //String msg = getString(R.string.msg_subscribed);
                                    if (!task.isSuccessful()) {
                                        //  msg = getString(R.string.msg_subscribe_failed);
                                    }
                                    //Log.d(TAG, msg);
                                }
                            });
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    Log.d("subfail2", Integer.toString(errorCode));
                }
            });
        }
        else {
            SubscriptionApi.deleteSubscriptionBySubsId(subscriptionModel.subs_id, new SubscriptionApi.MyCallback() {
                @Override
                public void onSuccess(SubscriptionModel subscriptionModel) {
                    Log.d("subSuccess", "success");
                }

                @Override
                public void onFail(int errorCode, Exception e) {
                    Log.d("subfail3", Integer.toString(errorCode));
                }
            });
            state=-1;
            Log.d("subSuccess2", Integer.toString(state));
            btn_subscription.setText("구독");

            FirebaseMessaging.getInstance().unsubscribeFromTopic(restaurantProductModel.res_id);
        }
    }



    private void getResModelFromProduct(RestaurantProductModel restaurantProductModel) {
        RestaurantApi.getUserById(restaurantProductModel.res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                if(restaurantModel.res_name != null) txt_salesman.setText(restaurantModel.res_name);
                rating.setRating(restaurantModel.res_rating);
                txt_rating.setText(Float.toString(restaurantModel.res_rating));
//                if(restaurantModel.res_address != null) txt_address.setText("동네: " + restaurantModel.res_address);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        txt_category = (TextView) findViewById(R.id.saleActivity_textView_categoryBig);
        txt_description = (TextView) findViewById(R.id.saleActivity_textView_description);
        txt_expireDate = (TextView) findViewById(R.id.saleActivity_textView_expiredDate);
        txt_quality = (TextView) findViewById(R.id.saleActivity_textView_quality);
        txt_quantity = (TextView) findViewById(R.id.saleActivity_textView_quantity);
        txt_salesman = (TextView) findViewById(R.id.saleActivity_textView_salesman);
        txt_title = (TextView) findViewById(R.id.saleActivity_textView_title);
        image_product = (ImageView) findViewById(R.id.saleActivity_imageView_product);
        btn_subscription = (Button) findViewById(R.id.saleActivity_button_subscription);
        txt_address = (TextView) findViewById(R.id.saleActivity_textView_address);
        rating= (RatingBar)findViewById((R.id.saleActivity_item_ratingBar_grade));  //!!!!!!!
        txt_rating=(TextView)findViewById(R.id.saleActivity_textView_ratingText);
        txt_cost=(TextView)findViewById((R.id.saleActivity_textView_cost));
        share=(Button)findViewById(R.id.saleActivity_button_share);
        Intent intent = getIntent();
        final RestaurantProductModel restaurantProductModel = (RestaurantProductModel)intent.getSerializableExtra("Model");
        //인탠트에서 프로덕트 모델을 받아옴.

        btn_buy = (Button) findViewById(R.id.saleActivity_button_buy);
        btn_chat = (Button) findViewById(R.id.saleActivity_button_chat);
        btn_evaluate = (Button) findViewById(R.id.test_ratingbtn);




        if(restaurantProductModel.completed!=-1) {
            btn_buy.setVisibility(View.INVISIBLE);
            btn_chat.setVisibility(View.INVISIBLE);
        }

        if(restaurantProductModel.completed==0) btn_evaluate.setVisibility(View.VISIBLE);

        getResModelFromProduct(restaurantProductModel);
        getSubscribeCheck(restaurantProductModel);


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.saleActivity_map_view);
        mapViewContainer.addView(mapView);

        Double lat=intent.getExtras().getDouble("mLat");
        Double lng=intent.getExtras().getDouble("mLng");


        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lng), 1, true);


        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName("Custom Marker");
        customMarker.setTag(1);
        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.marker2); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(customMarker);

        txt_title.setText(restaurantProductModel.title);
        txt_category.setText(restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
        //txt_salesman.setText("판매자 : "+r_name); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
        txt_quantity.setText(restaurantProductModel.quantity);
        if (restaurantProductModel.quality==1){
            txt_quality.setText("하");
        }
        else if (restaurantProductModel.quality==2){
            txt_quality.setText("중");
        }
        else if (restaurantProductModel.quality==3){
            txt_quality.setText("상");
        }
        txt_expireDate.setText(restaurantProductModel.expiration_date);
        txt_description.setText(restaurantProductModel.p_description);
        String c=Integer.toString(restaurantProductModel.cost);
        txt_cost.setText(c);
//        rating.setRating(4);      //레이팅 모델이 없음!!!!!!!!!
//        txt_rating.setText("4.0");     // 마찬가지!!!!!!!!!!!!!!

        String addressString = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREAN);
        try {
            List<Address> addresses = geocoder.getFromLocation(restaurantProductModel.latitude, restaurantProductModel.longitude, 10);
            for (int i=0; i<addresses.size(); i++) {
                if(addresses.get(i).getThoroughfare() != null ) {
                    txt_address.setText(addresses.get(i).getThoroughfare());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(getApplicationContext()).load(restaurantProductModel.p_imageURL).into(image_product);

        btn_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeClick(restaurantProductModel);
                Log.d("subs4","구독");
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSale(restaurantProductModel);
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                intent.putExtra("Model", restaurantProductModel);
                startActivity(intent);
                finish();
            }
        });

        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
                intent.putExtra("Model", restaurantProductModel);
                startActivity(intent);
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");

                String Test_Message = "공유할 Text";

                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);

                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
            }
        });

    }

}

