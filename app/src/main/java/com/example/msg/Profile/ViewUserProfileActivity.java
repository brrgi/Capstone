package com.example.msg.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.R;

public class ViewUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "ViewUserProfileActivity";

    private TextView name;
    private TextView sex;
    private TextView age;
    private TextView number;
    private TextView address;
    private RatingBar grade;

    private String user_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuserprofile);

        name=(TextView)findViewById(R.id.viewUserProfileActivity_textView_name);
        sex=(TextView)findViewById(R.id.viewUserProfileActivity_textView_sex);
        age=(TextView)findViewById(R.id.viewUserProfileActivity_textView_age);
        number=(TextView)findViewById(R.id.viewUserProfileActivity_textView_number);
        address=(TextView)findViewById(R.id.viewUserProfileActivity_textView_address);
        grade=(RatingBar)findViewById(R.id.viewUserProfileActivity_ratingBar_grade);

        Intent intent=getIntent();
        user_id=intent.getStringExtra("user_id");

        UserApi.getUserById(user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                name.setText(userModel.user_name);
                if(userModel.is_male)
                    sex.setText("남자");
                else
                    sex.setText("여자");
                age.setText(Integer.toString(2021 - userModel.age));
                number.setText(userModel.user_phone);
                grade.setRating(userModel.user_rating);
                address.setText(userModel.user_address);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }
}
