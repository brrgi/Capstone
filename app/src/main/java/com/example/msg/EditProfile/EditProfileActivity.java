package com.example.msg.EditProfile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.Map.DaumWebViewActivity;
import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private static final int SERACH_ADDRESS_ACTIVITY = 10000;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText phone;
    private EditText et_address;
    private EditText et_address_detail;
    private Button address;
    private Button edit;
    private Button birthyear;
    private int bornyear;
    private Double lati;
    private Double longi;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        uid = AuthenticationApi.getCurrentUid();

        address = (Button) findViewById(R.id.editProfileActivity_button_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent, SERACH_ADDRESS_ACTIVITY);
            }
        });

        et_address = (EditText) findViewById(R.id.editProfileActivity_edittext_address);
        et_address_detail = (EditText) findViewById(R.id.editProfileActivity_edittext_address_detail);
        email = (EditText) findViewById(R.id.editProfileActivity_edittext_email);
        password = (EditText) findViewById(R.id.editProfileActivity_edittext_password);
        name = (EditText) findViewById(R.id.editProfileActivity_edittext_name);
        phone = (EditText) findViewById(R.id.editProfileActivity_edittext_phone);
        edit = (Button) findViewById(R.id.editProfileActivity_button_edit);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //초기 세팅값
        UserApi.getUserById(uid, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                email.setText(userModel.email);
                name.setText(userModel.user_name);
                phone.setText(userModel.user_phone);
                et_address.setText(userModel.user_address);
                et_address_detail.setText(userModel.user_address_detail);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        //비밀번호 6자리 이상일 때 버튼 누를 수 있고 색상도 회색에서 주황색으로
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().length()<6){
                    edit.setEnabled(false);
                    edit.setBackgroundColor(Color.parseColor("#D8D8D8"));
                    edit.setText("비밀번호 6자리 이상 입력해주세요.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(password.getText().toString().length()>=6){
                    edit.setEnabled(true);
                    edit.setBackgroundColor(Color.parseColor("#F39C12"));
                    edit.setText("수정");
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApi.getUserById(uid, new UserApi.MyCallback() {
                    @Override
                    public void onSuccess(UserModel userModel) {
                        //userModel 변경
                        userModel.email = email.getText().toString();
                        userModel.user_name = name.getText().toString();
                        userModel.user_phone = phone.getText().toString();
                        userModel.user_address = et_address.getText().toString();
                        userModel.user_address_detail = et_address_detail.getText().toString();
                        userModel.latitude=lati;
                        userModel.longitude=longi;
                        UserApi.updateUser(userModel, new UserApi.MyCallback() {
                            @Override
                            public void onSuccess(UserModel userModel) {
                                user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "회원정보 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                            //startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                            finish();
                                        }
                                        else{
                                            Log.d(TAG, "Error password not updated");
                                        }
                                    }
                                });


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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SERACH_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            String datas = data.getStringExtra("comeback");
            if (datas != null)
                et_address.setText(datas);
            Double lat = data.getDoubleExtra("comebacks", 0);
            Double lon = data.getDoubleExtra("comebackss", 0);
            if (datas != null) {
                lati = lat;
                longi = lon;
            }
        }
    }

}