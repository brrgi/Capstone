package com.example.msg.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.R;
import com.example.msg.ban.BanActivity;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private Button ban_button;
    private String reported_user_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        ban_button=(Button)findViewById(R.id.profile_button_ban);

        Intent intent=getIntent();
        reported_user_id=intent.getStringExtra("reported_user_id");
        ban_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this, BanActivity.class);
                intent.putExtra("reported_user_id",reported_user_id);
                startActivity(intent);
            }
        });

    }
}
