package com.example.msg.Upload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.GuideLineApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.R;
import com.example.msg.RecyclerView.QualitySelectActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductRestUploadActivity extends AppCompatActivity {

    private ImageView productImage;
    private EditText title, quantity, expireDate, cost, description;
    private Spinner bigCategory, smallCategory;
    private TextView qualityText;
    private Button qualityButton, submit, fast;

    private final ArrayList<String> smallCategories = new ArrayList<>();
    private  ArrayAdapter<String> smallCategoriesAdapter;
    private final RestaurantProductModel restaurantProductModel = new RestaurantProductModel();


    private final double defaultLatitude = 0, defaultLongitude = 0;
    private Uri imageUri = null;

    private final int PICK_FROM_ALBUM =100, QUALITY_SELECT = 101;

    private void initialize() {
        productImage = (ImageView)findViewById(R.id.product_rest_imageView_product);
        title = (EditText)findViewById(R.id.product_rest_editText_title);
        quantity = (EditText)findViewById(R.id.product_rest_editText_quantity);
        //expireDate = (EditText)findViewById(R.id.product_rest_editText_expireDate);
        cost = (EditText)findViewById(R.id.product_rest_editText_cost);
        description = (EditText)findViewById(R.id.product_rest_editText_description);

        bigCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryA);
        smallCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryB);

        qualityText = (TextView)findViewById(R.id.product_rest_textView_quality);

        qualityButton = (Button) findViewById(R.id.product_rest_button_quality);
        submit = (Button) findViewById(R.id.product_rest_button_submit);
        fast = (Button) findViewById(R.id.product_rest_button_fast);

        smallCategoriesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, smallCategories);
        smallCategory.setAdapter(smallCategoriesAdapter);

    }

    private void setRestaurantProductModelFromUI() {
        restaurantProductModel.title = title.getText().toString();
        restaurantProductModel.quantity = quantity.getText().toString();
        restaurantProductModel.expiration_date = expireDate.getText().toString();
        restaurantProductModel.cost = Integer.parseInt(cost.getText().toString());
        restaurantProductModel.p_description = description.getText().toString();

        restaurantProductModel.categoryBig = bigCategory.getSelectedItem().toString();
        restaurantProductModel.categorySmall = smallCategory.getSelectedItem().toString();

        restaurantProductModel.completed = -1;

        restaurantProductModel.longitude = defaultLongitude;
        restaurantProductModel.latitude = defaultLatitude;
    }

    private void postRestProduct(RestaurantProductModel restaurantProductModel) {
        final String uid = AuthenticationApi.getCurrentUid();
        restaurantProductModel.res_id = uid;
        RestaurantProductApi.postProduct(restaurantProductModel, imageUri, new RestaurantProductApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantProductModel restaurantProductModel) {
                finish();
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_rest_upload);
        initialize();

        //대분류를 바꿀 때 소분류도 맞춰서 바꾸는 부분.
        bigCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //앨범에서 식재료 사진을 가져오는 부분
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        //품질 선택 버튼 누를 때
        qualityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductRestUploadActivity.this, QualitySelectActivity.class);
                intent.putExtra("category", smallCategory.getSelectedItem().toString());
                startActivityForResult(intent, QUALITY_SELECT);
            }
        });

        //제출 버튼을 누를 때
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRestaurantProductModelFromUI();
                restaurantProductModel.fast = false;
                postRestProduct(restaurantProductModel);
            }
        });

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRestaurantProductModelFromUI();
                restaurantProductModel.fast = true;
                postRestProduct(restaurantProductModel);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_FROM_ALBUM && resultCode==RESULT_OK){
            imageUri=data.getData();    //이미지 원본 경로
            productImage.setImageURI(imageUri);
        } else if(requestCode == QUALITY_SELECT && resultCode == RESULT_OK) {
            int quality = -1;
            if(data.hasExtra("quality")) quality = data.getIntExtra("quality",-1);
            restaurantProductModel.quality = quality;
            qualityText.setText(Integer.toString(quality));
        }
    }
}
