package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button ratingBtn;
    private float ratingScore = 0;


    private void ratingCalculate(UserProductModel userProductModel, final float ratingScore) {
        UserApi.getUserById(userProductModel.user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                userModel.user_rating = Math.round(((userModel.user_rating + ratingScore)/(userModel.ratingCount))*10)/10;
                userModel.ratingCount++;
                UserApi.updateUser(userModel, new UserApi.MyCallback() {
                    @Override
                    public void onSuccess(UserModel userModel) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Intent intent = getIntent();
        final UserProductModel userProductModel = (UserProductModel)intent.getSerializableExtra("Model");


        ratingBar = (RatingBar) findViewById(R.id.ratingActivity_ratingBar_rating);
        ratingBtn = (Button) findViewById(R.id.ratingActivity_ratingButton_rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingScore = v;
            }
        });


        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
