package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
<<<<<<< Updated upstream:app/src/main/java/com/example/msg/SaleActivity.java
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
=======

>>>>>>> Stashed changes:app/src/main/java/com/example/msg/SaleResActivity.java
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.annotation.Nullable;


public class SaleResActivity extends AppCompatActivity {

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

<<<<<<< Updated upstream:app/src/main/java/com/example/msg/SaleActivity.java
=======
    String r_name = "";

    private void getSubscribeCheck(final RestaurantProductModel restaurantProductModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        SubscriptionApi.getSubscriptionListByUserId(uid, new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                for(int i =0;i < subscriptionModelArrayList.size();i++) {

                    if((subscriptionModelArrayList.get(i).res_id)==restaurantProductModel.res_id)
                    {
                        btn_subscription.setText("구독 해지");
                    }
                    else {
                        btn_subscription.setText("구독");
                    }
                }
            }
            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }


    private void getResModelFromProduct(RestaurantProductModel restaurantProductModel) {
        RestaurantApi.getUserById(restaurantProductModel.res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                if(restaurantModel.res_name != null) txt_salesman.setText("판매자: " + restaurantModel.res_name);
                if(restaurantModel.res_address != null) txt_address.setText("동네: " + restaurantModel.res_address);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

>>>>>>> Stashed changes:app/src/main/java/com/example/msg/SaleResActivity.java

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
        final RestaurantProductModel restaurantProductModel = (RestaurantProductModel)intent.getSerializableExtra("Model");
        //인탠트에서 프로덕트 모델을 받아옴.

<<<<<<< Updated upstream:app/src/main/java/com/example/msg/SaleActivity.java


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
        txt_salesman.setText("판매자 : 우석이네 치킨집"); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
=======
        getResModelFromProduct(restaurantProductModel);
        getSubscribeCheck(restaurantProductModel);

        txt_title.setText("제목 : " + restaurantProductModel.title);
        txt_category.setText("카테고리 : " + restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
>>>>>>> Stashed changes:app/src/main/java/com/example/msg/SaleResActivity.java
        txt_quantity.setText("양 : " + restaurantProductModel.quantity);
        txt_quality.setText("품질 : " + restaurantProductModel.quality);
        txt_expireDate.setText("유통기한 : " + restaurantProductModel.expiration_date);
        txt_description.setText("상세설명 : " + restaurantProductModel.p_description);
        Glide.with(getApplicationContext()).load(restaurantProductModel.p_imageURL).into(image_product);

        btn_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                SubscriptionApi.getSubscriptionListByUserId(uid, new SubscriptionApi.MyListCallback() {
                    @Override
                    public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                        for(int i =0;i < subscriptionModelArrayList.size();i++) {

                            if((subscriptionModelArrayList.get(i).res_id)==restaurantProductModel.res_id)
                            {
                                //deleteSubcription 필요!!
                                btn_subscription.setText("구독");
                            }
                            else {
                                SubscriptionModel subscriptionModel = new SubscriptionModel();
                                SubscriptionApi.postSubscription(subscriptionModel, new SubscriptionApi.MyCallback() {
                                    @Override
                                    public void onSuccess(SubscriptionModel subscriptionModel) {
                                        Toast.makeText(getApplicationContext(), "구독 완료", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFail(int errorCode, Exception e) {

                                    }
                                });
                                btn_subscription.setText("구독 해지");
                            }
                        }
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
                Intent intent = new Intent(SaleResActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
    }


}

