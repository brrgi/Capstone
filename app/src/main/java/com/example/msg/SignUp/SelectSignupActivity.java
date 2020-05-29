package com.example.msg.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.msg.R;


public class SelectSignupActivity extends AppCompatActivity {

    private Button signup_user;
    private Button signup_restaurant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_signup);

        signup_user=(Button)findViewById(R.id.SelectSignupActivity_button_user);
        signup_restaurant=(Button)findViewById(R.id.SelectSignupActivity_button_restaurant);

        signup_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(SelectSignupActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
            }
        });

        signup_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        startActivity(new Intent(SelectSignupActivity.this, SignupRestActivity.class));
                        finish();
                    }
        });
    }

}
