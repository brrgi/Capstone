package com.example.msg.Sale;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.msg.ChatRoom.ChatRoomActivity;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.DatabaseModel.ShareModel;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ShareApi;
import com.example.msg.Api.UserApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.Map.MapActivity;
import com.example.msg.Profile.UserProfileActivity;
import com.example.msg.R;
import com.example.msg.RatingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SaleUserActivity extends AppCompatActivity {

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
    private ImageView image_product;
    private Button btn_rating;
    private TextView ban;
    private RatingBar grade;
    private Button love;
    private Button share;
    private double defaultLongitude = 0;
    private double defaultLatitude = 0;
    String u_name = "";
    String u_address="";
    String u_uid;

    private static int current = -1;

    private void processShare(final UserProductModel userProductModel, UserModel userModel) {
        ShareModel shareModel = new ShareModel();
        shareModel.share_from = userModel.user_id;
        shareModel.share_to = AuthenticationApi.getCurrentUid();
        shareModel.uproduct_id = userProductModel.uproduct_id;
        userProductModel.completed = 0;


        ShareApi.postShare(shareModel, new ShareApi.MyCallback() {
            @Override
            public void onSuccess(ShareModel shareModel) {
                //성공
                UserProductApi.updateProduct(userProductModel, new UserProductApi.MyCallback() {
                    @Override
                    public void onSuccess(UserProductModel userProductModel) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(userProductModel.title);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                //실패
            }
        });

    }

    private void processSale(RestaurantProductModel restaurantProductModel, RestaurantModel restaurantModel) {
        SaleModel saleModel = new SaleModel();
        saleModel.res_id = restaurantModel.res_id;
        saleModel.user_id = AuthenticationApi.getCurrentUid();
        saleModel.product_id = restaurantProductModel.rproduct_id;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleuser);

        txt_category = (TextView) findViewById(R.id.saleUserActivity_textView_categoryBig);
        txt_description = (TextView) findViewById(R.id.saleUserActivity_textView_description);
        txt_expireDate = (TextView) findViewById(R.id.saleUserActivity_textView_expiredDate);
        txt_quality = (TextView) findViewById(R.id.saleUserActivity_textView_quality);
        txt_quantity = (TextView) findViewById(R.id.saleUserActivity_textView_quantity);
        txt_salesman = (TextView) findViewById(R.id.saleUserActivity_textView_salesman);
        txt_address = (TextView)findViewById(R.id.saleUserActivity_textView_address);
        txt_title = (TextView) findViewById(R.id.saleUserActivity_textView_title);
        image_product = (ImageView) findViewById(R.id.saleUserActivity_imageView_product);

        btn_buy = (Button) findViewById(R.id.saleUserActivity_button_buy);
        btn_chat = (Button) findViewById(R.id.saleUserActivity_button_chat);
        btn_rating = (Button)findViewById(R.id.saleUserActivity_button_evaluate);
        share=(Button)findViewById(R.id.saleUserActivity_button_share);
        ban = (TextView) findViewById(R.id.saleUserActivity_textView_ban);
        grade=(RatingBar)findViewById(R.id.saleUserActivity_ratingBar_grade);
        txt_rating=(TextView) findViewById(R.id.saleUserActivity_textView_ratingText);
        Intent intent = getIntent();
        final UserProductModel userProductModel = (UserProductModel)intent.getSerializableExtra("Model");
        //인탠트에서 프로덕트 모델을 받아옴.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        if(uid.equals(userProductModel.user_id)) {
            btn_buy.setVisibility(View.INVISIBLE);
            btn_chat.setVisibility(View.INVISIBLE);
            btn_rating.setVisibility(View.INVISIBLE);
        }

        if(userProductModel.completed!=-1) {
            btn_chat.setVisibility(View.INVISIBLE);
            btn_buy.setVisibility(View.INVISIBLE);
        }
        if(userProductModel.completed==0) btn_rating.setVisibility(View.VISIBLE);

        getAddress(uid);
        UserApi.getUserById(userProductModel.user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                u_name = userModel.user_name;
                u_address=userModel.user_address;
                u_uid = userModel.user_id;
                txt_salesman.setText(u_name);
                ban.setText("신고 횟수 "+userModel.ban_count+"회");
                grade.setRating(userModel.user_rating);
                txt_rating.setText(Float.toString(userModel.user_rating));
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        txt_title.setText(userProductModel.title);
        txt_category.setText(userProductModel.categoryBig + " -> " + userProductModel.categorySmall);
       // txt_salesman.setText("판매자 : "+u_name); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
        txt_quantity.setText(userProductModel.quantity);

        if (userProductModel.quality==1){
            txt_quality.setText("하");
        }
        else if (userProductModel.quality==2){
            txt_quality.setText("중");
        }
        else if (userProductModel.quality==3){
            txt_quality.setText("상");
        }
        txt_expireDate.setText(userProductModel.expiration_date);
        txt_description.setText(userProductModel.p_description);

        Glide.with(getApplicationContext()).load(userProductModel.p_imageURL).into(image_product);


        String addressString = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREAN);
        try {
            List<Address> addresses = geocoder.getFromLocation(userProductModel.latitude, userProductModel.longitude, 10);
            for (int i=0; i<addresses.size(); i++) {
                if(addresses.get(i).getThoroughfare() != null ) {
                    txt_address.setText(addresses.get(i).getThoroughfare());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleUserActivity.this, ChatRoomActivity.class);
                intent.putExtra("id", userProductModel.user_id);
                startActivity(intent);
            }
        });


        txt_salesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleUserActivity.this, UserProfileActivity.class);
                intent.putExtra("reported_user_id",u_uid);
                startActivity(intent);
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SaleUserActivity.this, PayActivity.class);
                //startActivity(intent);
                UserModel userModel = new UserModel();
                userModel.user_id = u_uid;
                processShare(userProductModel, userModel);
            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
                intent.putExtra("Model", userProductModel);

                intent.putExtra("mLat",defaultLatitude);
                intent.putExtra("mLng", defaultLongitude);
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
}

