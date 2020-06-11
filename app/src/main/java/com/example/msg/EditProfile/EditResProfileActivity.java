package com.example.msg.EditProfile;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.MainActivity;
import com.example.msg.Map.DaumWebViewActivity;
import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class EditResProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditResProfileActivity";
    private static final int SERACH_ADDRESS_ACTIVITY = 10000;
    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText password;
    private EditText res_name;
    private EditText res_description;
    private EditText et_address;
    private EditText et_address_detail;
    private Button address;
    private Button edit;
    private EditText res_phone;
    private ImageView profile;
    private Uri imageUri;
    private EditText pickup_start_time;
    private EditText pickup_end_time;
    private Double lati;
    private Double longi;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editresprofile);
        uid = AuthenticationApi.getCurrentUid();


        profile = (ImageView) findViewById(R.id.editResProfileActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); //사진 가져오는 것
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        address = (Button) findViewById(R.id.editResProfileActivity_button_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditResProfileActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent, SERACH_ADDRESS_ACTIVITY);
            }
        });

        et_address = (EditText) findViewById(R.id.editResProfileActivity_edittext_address);
        et_address_detail = (EditText) findViewById(R.id.editResProfileActivity_edittext_address_detail);
        res_phone = (EditText) findViewById(R.id.editResProfileActivity_edittext_res_phone);
        email = (EditText) findViewById(R.id.editResProfileActivity_edittext_email);
        password = (EditText) findViewById(R.id.editResProfileActivity_edittext_password);
        res_name = (EditText) findViewById(R.id.editResProfileActivity_edittext_res_name);
        res_description = (EditText) findViewById(R.id.editResProfileActivity_edittext_res_description);
        pickup_start_time = (EditText) findViewById(R.id.editResProfileActivity_edittext_pickupstarttime);
        pickup_end_time = (EditText) findViewById(R.id.editResProfileActivity_edittext_pickupendtime);

        edit = (Button) findViewById(R.id.editResProfileActivity_button_edit);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //FirebaseStorage 초기화
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("restaurantImages").child(uid);

        //초기 세팅값
        RestaurantApi.getUserById(uid, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                email.setText(restaurantModel.res_email);
                res_name.setText(restaurantModel.res_name);
                res_phone.setText(restaurantModel.res_phone);
                et_address.setText(restaurantModel.res_address);
                et_address_detail.setText(restaurantModel.res_address_detail);
                pickup_start_time.setText(restaurantModel.pickup_start_time);
                pickup_end_time.setText(restaurantModel.pickup_end_time);
                res_description.setText(restaurantModel.res_description);
                Glide.with(EditResProfileActivity.this).load(restaurantModel.res_imageURL).into(profile);
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
                if (password.getText().toString().length() < 6) {
                    edit.setEnabled(false);
                    edit.setBackgroundColor(Color.parseColor("#D8D8D8"));
                    edit.setText("비밀번호 6자리 이상 입력해주세요.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password.getText().toString().length() >= 6) {
                    edit.setEnabled(true);
                    edit.setBackgroundColor(Color.parseColor("#F39C12"));
                    edit.setText("수정");
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //firestore storage 사진 업로드
                FirebaseStorage.getInstance().getReference().child("restaurantImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                        Log.d(TAG,"upload image");
                        while (!imageUrl.isComplete());
                        Log.d(TAG,"complete to upload image");
                        RestaurantApi.getUserById(uid, new RestaurantApi.MyCallback() {
                            @Override
                            public void onSuccess(RestaurantModel restaurantModel) {
                                //restaurantModel 변경
                                restaurantModel.res_email = email.getText().toString();
                                restaurantModel.res_name = res_name.getText().toString();
                                restaurantModel.res_phone = res_phone.getText().toString();
                                restaurantModel.res_address = et_address.getText().toString();
                                restaurantModel.res_address_detail = et_address_detail.getText().toString();
                                restaurantModel.pickup_start_time = pickup_start_time.getText().toString();
                                restaurantModel.pickup_end_time = pickup_end_time.getText().toString();
                                restaurantModel.res_description = res_description.getText().toString();
                                restaurantModel.res_imageURL = (imageUrl.getResult().toString());
                                Log.d(TAG,"update restaurantModel ");

                                RestaurantApi.updateRestaurant(restaurantModel, new RestaurantApi.MyCallback() {
                                    @Override
                                    public void onSuccess(RestaurantModel restaurantModel) {
                                        Log.d(TAG,"update restaurantModel complete not password");
                                        user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "식당정보 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(EditResProfileActivity.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFail(int errorCode, Exception e) {
                                        Log.d(TAG,"RestaurantApi update fail");
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();    //이미지 원본 경로
            profile.setImageURI(imageUri);
        }
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





