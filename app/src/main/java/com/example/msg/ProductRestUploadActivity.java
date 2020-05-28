package com.example.msg;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.Domain.AuthenticationApi;
import com.example.msg.Domain.GuideLineApi;
import com.example.msg.Domain.RestaurantProductApi;
import com.example.msg.recyclerView.QualitySelectActivity;

import java.util.ArrayList;

public class ProductRestUploadActivity extends AppCompatActivity {

    private ImageView productImage;
    private EditText title, quantity, cost, description;
    private Spinner bigCategory, smallCategory;
    private TextView qualityText;
    private Button qualityButton, submit, fast;
    private Button expireDate;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    private TextView txtResult;
    private Button address1;
    private Button address2;

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
        cost = (EditText)findViewById(R.id.product_rest_editText_cost);
        description = (EditText)findViewById(R.id.product_rest_editText_description);
        expireDate =(Button)findViewById(R.id.product_rest_button_expireDate);

        bigCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryA);
        smallCategory = (Spinner)findViewById(R.id.product_rest_spinner_categoryB);

        qualityText = (TextView)findViewById(R.id.product_rest_textView_quality);

        qualityButton = (Button) findViewById(R.id.product_rest_button_quality);
        submit = (Button) findViewById(R.id.product_rest_button_submit);
        fast = (Button) findViewById(R.id.product_rest_button_fast);
        smallCategoriesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, smallCategories);
        smallCategory.setAdapter(smallCategoriesAdapter);

        address1=(Button)findViewById(R.id.product_rest_button_address);
        address2=(Button)findViewById(R.id.product_rest_button_address2);
        txtResult = (TextView)findViewById(R.id.product_rest_TextView_txtResult);

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
        String uid = AuthenticationApi.getCurrentUid();
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
        this.InitializeListener();
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

        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( ProductRestUploadActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "고도  : " + altitude);

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }


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
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2019, 5, 24);

        dialog.show();
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

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

}
