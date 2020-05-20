package com.example.msg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.AuthenticationApi;
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.firebase.ui.auth.User;

public class ProductUploadActivity extends AppCompatActivity {

    private ImageView productImage = null;
    private EditText categoryBig = null;
    private EditText title = null;
    private Button submitButton = null;

    private Uri imageUri = null;

    private final int PICK_FROM_ALBUM = 100;


    private void postUserProduct(final UserProductModel userProductModel) {
        UserApi.getUserById(AuthenticationApi.getCurrentUid(), new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                userProductModel.user_id = userModel.user_id;
                UserProductApi.postProduct(userProductModel, imageUri, new UserProductApi.MyCallback() {
                   @Override
                   public void onSuccess(UserProductModel userProductModel) {
                        //유저 프로덕트 올리는데 성공함.
                       finish();
                   }

                   @Override
                   public void onFail(int errorCode, Exception e) {
                       Log.d("1234", Integer.toString(errorCode));
                        //유저 프로덕트 올리는데 실패함.
                   }
               });
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                //유저 정보를 불러오는데 실패한 경우.
            }
        });
    }
    /*
    입력: userProduct 모델과 User 모델, 콜백함수.
    출력: 없음
    동작: 입력으로 들어온 userProductModel, userModel, Image 정보를 이용해서 데이터베이스에 정보를 등록하면서 동시에 이미지를 저장합니다.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_upload);

        productImage = (ImageView) findViewById(R.id.product_upload_imageView_product);
        submitButton = (Button) findViewById(R.id.product_upload_button_submit);

        final UserProductModel userProductModel = new UserProductModel();
        userProductModel.categoryBig = "육류";
        userProductModel.categorySmall = "닭고기";
        userProductModel.completed = false;
        userProductModel.expiration_date = "2019-02-02";
        userProductModel.latitude = 5.0;
        userProductModel.longitude = 5.0;
        userProductModel.quality = 0;
        userProductModel.quantity = "많음";
        userProductModel.title = "싱싱한 닭고기";
        userProductModel.p_description = "싱싱한 닭고기입니다";

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        //앨범에서 식재료 사진을 가져오는 부분.



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUserProduct(userProductModel);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_FROM_ALBUM && resultCode==RESULT_OK){
            imageUri=data.getData();    //이미지 원본 경로
            productImage.setImageURI(imageUri);
        }
    }



}
