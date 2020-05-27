package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.msg.Domain.UserApi;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button ratingBtn;
    private float ratingScorre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = (RatingBar) findViewById(R.id.ratingActivity_ratingBar_rating);
        ratingBtn = (Button) findViewById(R.id.ratingActivity_ratingButton_rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingScorre = v;
            }
        });

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user api로 업데이트
            }
        });

    }
}
