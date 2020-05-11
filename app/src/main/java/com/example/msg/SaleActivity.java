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
import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SubscriptionModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.ReserveApi;
import com.example.msg.Domain.RestaurantProductApi;
import com.example.msg.Domain.SubscriptionApi;
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;


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


        RestaurantProductModel restaurantProductModel = new RestaurantProductModel();
        restaurantProductModel = RestaurantProductApi.makeDummy();
        txt_title.setText("제목 : " + restaurantProductModel.title);
        txt_category.setText("카테고리 : " + restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
        txt_salesman.setText("판매자 : 우석이네 치킨집"); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
        txt_quantity.setText("양 : " + restaurantProductModel.quantity);
        txt_quality.setText("품질 : " + restaurantProductModel.quality);
        txt_expireDate.setText("유통기한 : " + restaurantProductModel.expiration_date);
        txt_description.setText("상세설명 : " + restaurantProductModel.p_description);
        Glide.with(getApplicationContext()).load(restaurantProductModel.p_imageURL).into(image_product);

        btn_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                SubscriptionModel subscriptionModel = new SubscriptionModel();
                subscriptionModel.user_id = uid;
                subscriptionModel.res_id = "2";// 임시 테스트 GETrestaurant에서 res_uid 받아와야함
                SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
                    @Override
                    public void onSuccess(SubscriptionModel subscriptionModel) {
                        Toast.makeText(SaleActivity.this,"구독 완료",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFail(int errorCode, Exception e) {
                    }
                });
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

