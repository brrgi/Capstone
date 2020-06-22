package com.example.msg.Upload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msg.Api.RestaurantApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.GuideLineApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.R;
import com.example.msg.RecyclerView.QualitySelectActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductRestUploadActivity extends AppCompatActivity {

    private ImageView productImage;
    private EditText title, quantity, cost, description, stock;
    private Spinner bigCategory, smallCategory;
    private TextView qualityText,txtResult;
    private Button qualityButton, submit, fast,expireDate;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    private final ArrayList<String> smallCategories = new ArrayList<>();
    private  ArrayAdapter<String> smallCategoriesAdapter;
    private final RestaurantProductModel restaurantProductModel = new RestaurantProductModel();


    private double defaultLatitude = 0.0, defaultLongitude = 0.0;
    private Uri imageUri = null;

    private final int PICK_FROM_ALBUM =100, QUALITY_SELECT = 101;

    private void initialize() {
        productImage = (ImageView)findViewById(R.id.product_rest_imageView_product);
        title = (EditText)findViewById(R.id.product_rest_editText_title);
        quantity = (EditText)findViewById(R.id.product_rest_editText_quantity);
        expireDate = (Button)findViewById(R.id.product_rest_button_expireDate);
        cost = (EditText)findViewById(R.id.product_rest_editText_cost);
        description = (EditText)findViewById(R.id.product_rest_editText_description);
        stock = (EditText)findViewById(R.id.product_rest_editText_stock);

        bigCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryA);
        smallCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryB);

        qualityText = (TextView)findViewById(R.id.product_rest_textView_quality);

        qualityButton = (Button) findViewById(R.id.product_rest_button_quality);
        submit = (Button) findViewById(R.id.product_rest_button_submit);
        fast = (Button) findViewById(R.id.product_rest_button_fast);

        smallCategoriesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, smallCategories);
        smallCategory.setAdapter(smallCategoriesAdapter);
        txtResult = (TextView)findViewById(R.id.product_rest_TextView_txtResult);

    }

    private void setRestaurantProductModelFromUI() {
        restaurantProductModel.title = title.getText().toString();
        restaurantProductModel.quantity = quantity.getText().toString();
        restaurantProductModel.expiration_date = expireDate.getText().toString();
        restaurantProductModel.cost = Integer.parseInt(cost.getText().toString());
        restaurantProductModel.p_description = description.getText().toString();
        restaurantProductModel.stock = Integer.parseInt(stock.getText().toString());

        restaurantProductModel.categoryBig = bigCategory.getSelectedItem().toString();
        restaurantProductModel.categorySmall = smallCategory.getSelectedItem().toString();

        restaurantProductModel.completed = -1;
        restaurantProductModel.saleDateYear=-1;
        restaurantProductModel.saleDateMonth=-1;
        restaurantProductModel.saleDateDay=-1;
        Integer i = Integer.parseInt(qualityText.getText().toString());
        restaurantProductModel.quality=i;



    }

    private void postRestProduct(RestaurantProductModel restaurantProductModel) {
        final String uid = AuthenticationApi.getCurrentUid();
        restaurantProductModel.res_id = uid;
        RestaurantProductApi.postProduct(restaurantProductModel, imageUri, new RestaurantProductApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantProductModel restaurantProductModel) {
                Toast.makeText(getApplicationContext(), "상품 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
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
        this.InitializeListener();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        RestaurantApi.getUserById(uid, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                defaultLatitude=restaurantModel.res_latitude;
                defaultLongitude=restaurantModel.res_longitude;
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
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

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);




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
                Log.d("GOS", "제발"+defaultLongitude);
                restaurantProductModel.fast = false;
                restaurantProductModel.longitude = defaultLongitude;
                restaurantProductModel.latitude = defaultLatitude;
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



    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                expireDate.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");

            }
        };
    }



    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2020, 6, 3);

        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();    //이미지 원본 경로
            productImage.setImageURI(imageUri);
        } else if (requestCode == QUALITY_SELECT) {
            int quality = -1;
            if (data.hasExtra("quality")) quality = data.getIntExtra("quality", -1);
            restaurantProductModel.quality = quality;
            if (restaurantProductModel.quality==1){
                qualityText.setText("하");
            }
            else if (restaurantProductModel.quality==2){
                qualityText.setText("중");
            }
            else if (restaurantProductModel.quality==3){
                qualityText.setText("상");
            }
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            defaultLatitude = location.getLongitude();
            defaultLongitude = location.getLatitude();
            double altitude = location.getAltitude();


        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

}
