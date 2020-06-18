package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.Api.SaleApi;
import com.example.msg.Api.UserApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Sale.SaleUserActivity;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button ratingBtn;
    private float ratingScore = 0;
    private TextView saleRating;
    private TextView salesman;
    private EditText review;
    private float ratingSum = 0;

    private void ratingUserCalculate(UserProductModel userProductModel, final float ratingScore) {
        UserApi.getUserById(userProductModel.user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                ratingSum = (userModel.user_rating)*((userModel.ratingCount)-1);
                Log.d("RatingSum",Float.toString(ratingSum));
                Log.d("RatingSum1",Float.toString(ratingScore));
                userModel.user_rating = Math.round(((ratingSum + ratingScore)/(userModel.ratingCount))*10)/10;
                userModel.ratingCount++;
                UserApi.updateUser(userModel, new UserApi.MyCallback() {
                    @Override
                    public void onSuccess(UserModel userModel) {  }
                    @Override
                    public void onFail(int errorCode, Exception e) { }
                });
            }
            @Override
            public void onFail(int errorCode, Exception e) { }
        });
    }

    private void ratingResCalculate(RestaurantProductModel restaurantProductModel, final float ratingScore) {
        RestaurantApi.getUserById(restaurantProductModel.res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                ratingSum = (restaurantModel.res_rating)*((restaurantModel.ratingCount)-1);
                restaurantModel.res_rating = Math.round(((ratingSum + ratingScore)/(restaurantModel.ratingCount))*10)/10;
                restaurantModel.ratingCount++;
                RestaurantApi.updateRestaurant(restaurantModel, new RestaurantApi.MyCallback() {
                    @Override
                    public void onSuccess(RestaurantModel restaurantModel) { }

                    @Override
                    public void onFail(int errorCode, Exception e) { }
                });
            }
            @Override
            public void onFail(int errorCode, Exception e) { }
        });
    }

    private void setTextModel(Object obj) {
        if(obj instanceof UserProductModel) {
            UserProductModel userProductModel = (UserProductModel) obj;
            UserApi.getUserById(userProductModel.user_id, new UserApi.MyCallback() {
                @Override
                public void onSuccess(UserModel userModel) {
                    String tmp = Float.toString(userModel.user_rating);
                    saleRating.setText("판매자 평점 : " + tmp);
                    salesman.setText("판매자 : " + userModel.user_name);
                }

                @Override
                public void onFail(int errorCode, Exception e) {

                }
            });
        }
        else {
            RestaurantProductModel restaurantProductModel = (RestaurantProductModel) obj;
            RestaurantApi.getUserById(restaurantProductModel.res_id, new RestaurantApi.MyCallback() {
                @Override
                public void onSuccess(RestaurantModel restaurantModel) {
                    String tmp = Float.toString(restaurantModel.res_rating);
                    saleRating.setText("식당 평점 : " + tmp);
                    salesman.setText("식당 이름 : " + restaurantModel.res_name);
                }

                @Override
                public void onFail(int errorCode, Exception e) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        final Intent intent = getIntent();
        //final UserProductModel userProductModel = (UserProductModel)intent.getSerializableExtra("Model");
        final Object obj = (Object)intent.getSerializableExtra("Model");

        saleRating = (TextView) findViewById(R.id.ratingActivity_textview_rating);
        salesman = (TextView)findViewById(R.id.ratingActivity_textview_user);
        ratingBar = (RatingBar) findViewById(R.id.ratingActivity_ratingBar_rating);
        ratingBtn = (Button) findViewById(R.id.ratingActivity_ratingButton_rating);
        review=(EditText)findViewById(R.id.ratingActivity_editText_review);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingScore = v;
            }
        });

        setTextModel(obj);

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(obj instanceof UserProductModel) {
                    UserProductModel userProductModel = (UserProductModel)obj;
                    ratingUserCalculate(userProductModel,ratingScore);
                    userProductModel.completed = 1;

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNowYear = new SimpleDateFormat("yy");
                    SimpleDateFormat sdfNowMonth = new SimpleDateFormat("MM");
                    SimpleDateFormat sdfNowDay = new SimpleDateFormat("dd");
                    String formatDateYear = sdfNowYear.format(date);
                    String formatDateMonth = sdfNowMonth.format(date);
                    String formatDateDay = sdfNowDay.format(date);

                    userProductModel.saleDateYear=Integer.parseInt(formatDateYear);
                    userProductModel.saleDateMonth=Integer.parseInt(formatDateMonth);
                    userProductModel.saleDateDay=Integer.parseInt(formatDateDay);
                    Log.d("1234", formatDateYear+"Tlqkf"+formatDateMonth+formatDateDay);
                    UserProductApi.updateProduct(userProductModel, new UserProductApi.MyCallback() {
                        @Override
                        public void onSuccess(UserProductModel userProductModel) {
                            //Share update
                            finish();
                        }

                        @Override
                        public void onFail(int errorCode, Exception e) { }
                    });
                    finish();
                }
                else {
                    RestaurantProductModel restaurantProductModel = (RestaurantProductModel)obj;
                    ratingResCalculate(restaurantProductModel,ratingScore);
                    restaurantProductModel.completed = 1;
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNowYear = new SimpleDateFormat("yy");
                    SimpleDateFormat sdfNowMonth = new SimpleDateFormat("MM");
                    SimpleDateFormat sdfNowDay = new SimpleDateFormat("dd");
                    String formatDateYear = sdfNowYear.format(date);
                    String formatDateMonth = sdfNowMonth.format(date);
                    String formatDateDay = sdfNowDay.format(date);

                    restaurantProductModel.saleDateYear=Integer.parseInt(formatDateYear,10);
                    restaurantProductModel.saleDateMonth=Integer.parseInt(formatDateMonth,10);
                    restaurantProductModel.saleDateDay=Integer.parseInt(formatDateDay,10);

                    RestaurantProductApi.updateProduct(restaurantProductModel, new RestaurantProductApi.MyCallback() {
                        @Override
                        public void onSuccess(RestaurantProductModel restaurantProductModel) {
                            //Sales update
                            SaleApi.getSaleByProductIdandUserId(AuthenticationApi.getCurrentUid(), restaurantProductModel.rproduct_id, new SaleApi.MyListCallback() {
                                @Override
                                public void onSuccess(ArrayList<SaleModel> saleModels) {
                                    saleModels.get(0).review=review.getText().toString();
                                    SaleApi.updateSales(saleModels.get(0), new SaleApi.MyCallback() {
                                        @Override
                                        public void onSuccess(SaleModel saleModel) {
                                            Toast.makeText(getApplicationContext(), "평가가 완료되었습니다.", Toast.LENGTH_LONG).show();
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
                            finish();
                        }

                        @Override
                        public void onFail(int errorCode, Exception e) { }
                    });


                }
            }
        });

    }
}
