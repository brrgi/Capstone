package com.example.msg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SubscriptionModel;

import com.example.msg.Domain.RestaurantApi;
import com.example.msg.Domain.SubscriptionApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;



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
    private ImageView image_product;
    private Button btn_subscription;

    private static int current = -1;

    String r_name = "";


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
        btn_buy = (Button) findViewById(R.id.saleActivity_button_buy);
        btn_chat = (Button) findViewById(R.id.saleActivity_button_chat);
        image_product = (ImageView) findViewById(R.id.saleActivity_imageView_product);
        btn_subscription = (Button) findViewById(R.id.saleActivity_button_subscription);
        Intent intent = getIntent();
        RestaurantProductModel restaurantProductModel = (RestaurantProductModel)intent.getSerializableExtra("Model");
        //인탠트에서 프로덕트 모델을 받아옴.

        RestaurantApi.getUserById(restaurantProductModel.res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                r_name = restaurantModel.res_name;
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });


        /* RestaurantProductApi.getProduct(new RestaurantProductApi.MyCallback() {
           @Override
           public void onSuccess(RestaurantProductModel restaurantProductModel) {
               txt_title.setText("제목 : " + restaurantProductModel.title);
               txt_category.setText("카테고리 : " + restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
               txt_salesman.setText("판매자 : 우석이네 치킨집"); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
               txt_quantity.setText("양 : " + restaurantProductModel.quantity);
               txt_quality.setText("품질 : " + restaurantProductModel.quality);
               txt_expireDate.setText("유통기한 : " + restaurantProductModel.expiration_date);
               txt_description.setText("상세설명 : " + restaurantProductModel.p_description);
               Glide.with(getApplicationContext()).load(restaurantProductModel.p_imageURL).into(image_product);
           }
           @Override
           public void onFail(int errorCode, Exception e) {
               Log.d("sale error","error");
           }
         }, "1rdx4BsFHYHqtztzrPYb");*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        SubscriptionApi.getSubscriptionListByUserId(uid, new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                for(int i =0;i < subscriptionModelArrayList.size();i++) {

                    if((subscriptionModelArrayList.get(i).res_id)=="2")
                    {
                        btn_subscription.setText("구독 해지");
                    }
                }
                Log.d("array",String.valueOf(current));
            }
            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });


        txt_title.setText("제목 : " + restaurantProductModel.title);
        txt_category.setText("카테고리 : " + restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
        txt_salesman.setText("판매자 : "+r_name); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
        txt_quantity.setText("양 : " + restaurantProductModel.quantity);
        txt_quality.setText("품질 : " + restaurantProductModel.quality);
        txt_expireDate.setText("유통기한 : " + restaurantProductModel.expiration_date);
        txt_description.setText("상세설명 : " + restaurantProductModel.p_description);
        Glide.with(getApplicationContext()).load(restaurantProductModel.p_imageURL).into(image_product);

        btn_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(current) {
                    case -1:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String uid = user.getUid();
                        SubscriptionModel subscriptionModel = new SubscriptionModel();
                        subscriptionModel.user_id = uid;
                        subscriptionModel.res_id = "2";// 임시 테스트 GETrestaurant에서 res_uid 받아와야함
                        SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
                            @Override
                            public void onSuccess(SubscriptionModel subscriptionModel) {
                                Toast.makeText(SaleActivity.this,"구독 완료",Toast.LENGTH_SHORT).show();
                                btn_subscription.setText("구독 해지");
                                current = 0;
                            }
                            @Override
                            public void onFail(int errorCode, Exception e) {
                            }
                        });
                        break;
                    case 0:
                        btn_subscription.setText("구독!"); //여기에 delete들어가야 할듯
                        current = -1;
                        break;
                    default:
                        break;
                }

            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
    }


}

