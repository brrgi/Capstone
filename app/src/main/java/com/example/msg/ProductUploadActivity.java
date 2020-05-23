package com.example.msg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.AuthenticationApi;
import com.example.msg.Domain.GuideLineApi;
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.recyclerView.QualitySelectActivity;
import com.firebase.ui.auth.User;

import java.util.ArrayList;

public class ProductUploadActivity extends AppCompatActivity {

    private ImageView productImage = null;
    private EditText categoryBig = null;
    private EditText title = null;
    private Button submitButton = null;
    private Spinner bigSpinner = null;
    private Spinner smallSpinner = null;
    private EditText quality = null;
    private EditText quantity;
    private EditText specification = null;

    private Uri imageUri = null;

    private final int PICK_FROM_ALBUM = 100;
    private final int QUALITY_SELECT = 101;


    private void postUserProduct(final UserProductModel userProductModel) {
        UserApi.getUserById(AuthenticationApi.getCurrentUid(), new UserApi.MyCallback() {
            //유저 모델을 가져오는 함수를 호출.
            @Override
            public void onSuccess(UserModel userModel) {
                //성공하는 경우
                userProductModel.user_id = userModel.user_id;
                //userProductModel의 user_id 필드에 userModel의 user_id를 집어넣음.
                UserProductApi.postProduct(userProductModel, imageUri, new UserProductApi.MyCallback() {
                    //유저 프로덕트를 post하는 함수를 호출.
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
                Log.d("1234", Integer.toString(errorCode));
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
        smallSpinner = (Spinner)findViewById(R.id.product_upload_spinner_small);
        bigSpinner = (Spinner)findViewById(R.id.product_upload_spinner_big);
        quality = (EditText)findViewById(R.id.product_upload_editText_quality);
        specification = (EditText)findViewById(R.id.product_upload_editText_description);
        Button test = (Button)findViewById(R.id.product_upload_button_test);
        title = (EditText)findViewById(R.id.product_upload_editText_title);

        final ArrayList<String> smallCategories = new ArrayList<>();
        final ArrayAdapter<String> smallCategoriesAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, smallCategories);

        final UserProductModel userProductModel = new UserProductModel();
        smallSpinner.setAdapter(smallCategoriesAdapter);

        bigSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smallCategories.clear();
                smallCategories.addAll(GuideLineApi.getSmallCategoryList((String)parent.getItemAtPosition(position)));
                smallCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        //앨범에서 식재료 사진을 가져오는 부분.

        quality.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(ProductUploadActivity.this, QualitySelectActivity.class);
                    intent.putExtra("category", smallSpinner.getSelectedItem().toString());
                }
                return false;
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductUploadActivity.this, QualitySelectActivity.class);
                intent.putExtra("category", smallSpinner.getSelectedItem().toString());
                startActivityForResult(intent, QUALITY_SELECT);
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProductModel.title = title.getText().toString();
                userProductModel.p_description = specification.getText().toString();
                userProductModel.categorySmall = smallSpinner.getSelectedItem().toString();
                userProductModel.categoryBig = bigSpinner.getSelectedItem().toString();
                //userProductModel.quantity
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
