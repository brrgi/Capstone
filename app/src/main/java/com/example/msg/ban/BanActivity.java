package com.example.msg.ban;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.DatabaseModel.BanModel;
import com.example.msg.Profile.UserProfileActivity;
import com.example.msg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class BanActivity extends AppCompatActivity {
    private static final String TAG = "BanActivity";
    private Button submit;
    private EditText description;
    final BanModel banModel=new BanModel();
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        submit=(Button)findViewById(R.id.ban_button_submit);
        description=(EditText)findViewById(R.id.ban_editText_description);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                banModel.report_user_id=AuthenticationApi.getCurrentUid();
                banModel.reported_user_id=intent.getStringExtra("reported_user_id");
                banModel.description=description.getText().toString();
                banModel.time=calendar.getTime().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Ban")
                        .add(banModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "사용자 신고가 완료되었습니다.", Toast.LENGTH_LONG).show();

                                Log.d(TAG,"ban success");
                                startActivity(new Intent(BanActivity.this, UserProfileActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"ban fail");
                            }
                        });
            }
        });

    }
}
