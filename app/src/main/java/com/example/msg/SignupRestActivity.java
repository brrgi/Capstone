package com.example.msg;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


public class SignupRestActivity extends AppCompatActivity {


    private static final String TAG = "SignupRestActivity";
    private static final int SERACH_ADDRESS_ACTIVITY = 10000;
    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText password;
    private EditText res_name;
    private EditText res_description;
    private EditText et_address;
    private Button address;
    private Button signup;
    private String splash_background;
    private EditText res_phone;
    private ImageView profile;
    private Uri imageUri;
    private EditText pickup_start_time;
    private EditText pickup_end_time;
    private TimePickerDialog.OnTimeSetListener callbackMethod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_rest);

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        profile = (ImageView) findViewById(R.id.signupRestActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); //사진 가져오는 것
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        address=(Button)findViewById(R.id.signupRestActivity_button_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupRestActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent,SERACH_ADDRESS_ACTIVITY);
            }
        });

        et_address=(EditText)findViewById(R.id.signupRestActivity_edittext_address);
        res_phone = (EditText) findViewById(R.id.signupRestActivity_edittext_res_phone);
        email = (EditText) findViewById(R.id.signupRestActivity_edittext_email);
        password = (EditText) findViewById(R.id.signupRestActivity_edittext_password);
        res_name = (EditText) findViewById(R.id.signupRestActivity_edittext_res_name);
        res_description = (EditText) findViewById(R.id.signupRestActivity_edittext_res_description);
        pickup_start_time = (EditText) findViewById(R.id.signupRestActivity_edittext_pickupstarttime);
        pickup_end_time = (EditText) findViewById(R.id.signupRestActivity_edittext_pickupendtime);

        signup = (Button) findViewById(R.id.signupRestActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (email.getText().toString() == null || res_name.getText().toString() == null || password.getText().toString() == null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupRestActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("restaurantImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while (!imageUrl.isComplete()) ;
                                        com.example.msg.DatabaseModel.RestaurantModel restaurantModel=new com.example.msg.DatabaseModel.RestaurantModel();

                                        restaurantModel.res_name=(res_name.getText().toString());
                                        restaurantModel.res_imageURL=(imageUrl.getResult().toString());
                                        restaurantModel.res_phone=(res_phone.getText().toString());
                                        restaurantModel.res_address=(et_address.getText().toString());      //추가
                                        restaurantModel.res_description=(res_description.getText().toString());
                                        restaurantModel.pickup_start_time=(pickup_start_time.getText().toString());
                                        restaurantModel.pickup_end_time=(pickup_end_time.getText().toString());
                                        restaurantModel.res_id=uid;
                                        restaurantModel.approved=false;

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("Restaurant")
                                                .document(uid)
                                                .set(restaurantModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                                                        Log.d(TAG, "SUCCESS");
                                                        startActivity(new Intent(SignupRestActivity.this, LoginActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                                                        Log.d(TAG, "Faliure");
                                                    }
                                                });

                                    }
                                });

                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();    //이미지 원본 경로
            profile.setImageURI(imageUri);
        }
        if(requestCode==SERACH_ADDRESS_ACTIVITY && resultCode==RESULT_OK){
            String datas=data.getStringExtra("comeback");
            if (datas!=null)
                et_address.setText(datas);
        }
    }

//    public void InitializeListener()
//    {
//        callbackMethod = new TimePickerDialog.OnTimeSetListener()
//        {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
//            {
//                pickup_start_time.setText(hourOfDay + "시" + minute + "분");
//            }
//        };
//    }
//
//    public void OnClickHandler(View view) {
//        TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod, 8, 10, true);
//
//        dialog.show();
//    }
}