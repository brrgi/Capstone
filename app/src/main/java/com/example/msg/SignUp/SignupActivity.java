package com.example.msg.SignUp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.msg.Login.LoginActivity;
import com.example.msg.Map.DaumWebViewActivity;
import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int SERACH_ADDRESS_ACTIVITY = 10000;
    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText phone;
    private EditText et_address;
    private EditText et_address_detail;
    private Button address;
    private Button signup;
    private String splash_background;
    private Button birthyear;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private RadioButton man;
    //private RadioButton woman;
    private int bornyear;
    private Double lati;
    private Double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        address=(Button)findViewById(R.id.signupActivity_button_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent,SERACH_ADDRESS_ACTIVITY);
            }
        });

        et_address=(EditText)findViewById(R.id.signupActivity_edittext_address);
        et_address_detail=(EditText)findViewById(R.id.signupActivity_edittext_address_detail);
        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        phone=(EditText) findViewById(R.id.signupActivity_edittext_phone);
        birthyear = (Button) findViewById(R.id.signupActivity_button_birthyear);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));
        man = (RadioButton) findViewById(R.id.signupActivity_radiobutton_man);
        //woman = (RadioButton) findViewById(R.id.signupActivity_radiobutton_woman);
        this.InitializeListener();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                com.example.msg.DatabaseModel.UserModel userModel=new com.example.msg.DatabaseModel.UserModel();
                                //UserModel userModel = new UserModel();
                                userModel.user_name=(name.getText().toString());
                                userModel.user_phone=(phone.getText().toString());
                                userModel.user_address=(et_address.getText().toString());       //추가 이레 5.25
                                userModel.user_address_detail=(et_address_detail.getText().toString());
                                userModel.ban_count=0;
                                userModel.mileage=2;
                                userModel.user_grade=0;
                                userModel.user_id=uid;
                                userModel.sanction=false;
                                userModel.email=email.getText().toString();
                                if (man.isChecked())
                                    userModel.is_male=true;
                                else
                                    userModel.is_male=false;
                                userModel.latitude=lati;
                                userModel.longitude=longi;
                                userModel.age=bornyear;

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("User")
                                        .document(uid)
                                        .set(userModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                                                Log.d(TAG, "SUCCESS");
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
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

    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                birthyear.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
                bornyear = year;
            }
        };
    }

    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2019, 5, 24);

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SERACH_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            String datas = data.getStringExtra("comeback");
            if (datas != null)
                et_address.setText(datas);
            Double lat=data.getDoubleExtra("comebacks",0);
            Double lon=data.getDoubleExtra("comebackss",0);
            if (datas!=null){
                lati=lat;
                longi=lon;
            }
        }
    }

}