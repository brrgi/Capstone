package com.example.msg.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.R;
import com.example.msg.Sale.UserSaleProductsActivity;
import com.example.msg.ban.BanActivity;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private Button ban_button;
    private Button products_button;
    private String reported_user_id;
    private TextView user_name;
    private RatingBar grade;
    private TextView address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        ban_button=(Button)findViewById(R.id.profile_button_ban);
        products_button=(Button)findViewById(R.id.profile_button_products);
        user_name=(TextView)findViewById(R.id.profile_textView_name);
        grade=(RatingBar)findViewById(R.id.profile_item_ratingBar_grade);
        address=(TextView)findViewById(R.id.profile_textView_address);

        Intent intent=getIntent();
        reported_user_id=intent.getStringExtra("reported_user_id");



        UserApi.getUserById(reported_user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                user_name.setText(userModel.user_name);
                grade.setRating(userModel.user_rating);
                address.setText(userModel.user_address);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });


        ban_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(UserProfileActivity.this, BanActivity.class);
                        intent.putExtra("reported_user_id",reported_user_id);
                        startActivity(intent);
            }
        });

        products_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this, UserSaleProductsActivity.class);
                intent.putExtra("reported_user_id",reported_user_id);
                startActivity(intent);
            }
        });
    }
}
