package com.example.msg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.msg.model.UserModel;
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




public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int PICK_FROM_ALBUM = 10;
    private static final int SERACH_ADDRESS_ACTIVITY = 10000;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText phone;
    private EditText et_address;
    private Button address;
    private Button signup;
    private String splash_background;
    private ImageView profile;
    private Uri imageUri;
    private Button birthyear;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private RadioButton man;
    private RadioButton woman;
    private int bornyear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseRemoteConfig mFirebaseRemoteConfig= FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }


        profile=(ImageView)findViewById(R.id.signupActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); //사진 가져오는 것
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);

            }
        });
        address=(Button)findViewById(R.id.signupActivity_button_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent,SERACH_ADDRESS_ACTIVITY);
            }
        });

        et_address=(EditText)findViewById(R.id.signupActivity_edittext_address);
        email=(EditText)findViewById(R.id.signupActivity_edittext_email);
        password=(EditText)findViewById(R.id.signupActivity_edittext_password);
        name=(EditText)findViewById(R.id.signupActivity_edittext_name);
        phone=(EditText) findViewById(R.id.signupActivity_edittext_phone);
        birthyear = (Button) findViewById(R.id.signupActivity_button_birthyear);
        signup=(Button)findViewById(R.id.signupActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));
        man = (RadioButton) findViewById(R.id.signupActivity_radiobutton_man);
        woman = (RadioButton) findViewById(R.id.signupActivity_radiobutton_woman);
        this.InitializeListener();

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (email.getText().toString() == null || name.getText().toString() ==null || password.getText().toString() == null){
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while(!imageUrl.isComplete());


                                        UserModel userModel = new UserModel();
                                        userModel.setUserName(name.getText().toString());
                                        userModel.setProfileImageUrl(imageUrl.getResult().toString());
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users")
                                                .document(uid)
                                                .set(userModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                                                        Log.d(TAG,"SUCCESS");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                                                        Log.d(TAG,"Faliure");
                                                    }
                                                });
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
        if(requestCode==SERACH_ADDRESS_ACTIVITY && resultCode==RESULT_OK){
            String datas=data.getStringExtra("comeback");
            if (datas!=null)
                et_address.setText(datas);
        }
    }
}