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

import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.GuideLineApi;
import com.example.msg.Api.UserApi;
import com.example.msg.Api.UserProductApi;
import com.example.msg.R;
import com.example.msg.RecyclerView.QualitySelectActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProductUploadActivity extends AppCompatActivity {

    private ImageView productImage = null;
    private EditText categoryBig = null;
    private EditText title = null;
    private Button submitButton = null;
    private Spinner bigSpinner = null;
    private Spinner smallSpinner = null;
    private Button qualityButton = null;
    private TextView qualityText;
    private EditText quantity;
    private EditText specification = null;
    private Button expireDate;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private Uri imageUri = null;
    private Button address1;
    private Button address2;
    private final int PICK_FROM_ALBUM = 100;
    private final int QUALITY_SELECT = 101;
    private double longitude;
    private double latitude;
    private double altitude;

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
//product_upload_button_address2
        productImage = (ImageView) findViewById(R.id.product_upload_imageView_product);
        submitButton = (Button) findViewById(R.id.product_upload_button_submit);
        smallSpinner = (Spinner)findViewById(R.id.product_upload_spinner_categoryB);
        bigSpinner = (Spinner)findViewById(R.id.product_upload_spinner_categoryA);
        qualityButton = (Button)findViewById(R.id.product_upload_button_quality);
        specification = (EditText)findViewById(R.id.product_upload_editText_description);
        title = (EditText)findViewById(R.id.product_upload_editText_title);
        qualityText = (TextView)findViewById(R.id.product_upload_textView_quality);
        expireDate = (Button)findViewById(R.id.product_upload_button_expireDate);
        quantity = (EditText)findViewById(R.id.product_upload_editText_quantity);
        address1=(Button)findViewById(R.id.product_upload_button_address);
        address2=(Button)findViewById(R.id.product_upload_button_address2);

        this.InitializeListener();
        final double  defaultLongitude = 0, defaultLatitude = 0;
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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


        //앨범에서 식재료 사진을 가져오는 부분.
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        qualityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductUploadActivity.this, QualitySelectActivity.class);
                intent.putExtra("category", smallSpinner.getSelectedItem().toString());
                startActivityForResult(intent, QUALITY_SELECT);
            }
        });




        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                UserApi.getUserById(uid, new UserApi.MyCallback() {
                    @Override
                    public void onSuccess(UserModel userModel) {
                        latitude=userModel.latitude;
                        longitude=userModel.longitude;
                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });
            }
        });

        address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( ProductUploadActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    altitude = location.getAltitude();



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




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProductModel.title = title.getText().toString();
                userProductModel.p_description = specification.getText().toString();
                userProductModel.categorySmall = smallSpinner.getSelectedItem().toString();
                userProductModel.categoryBig = bigSpinner.getSelectedItem().toString();
                userProductModel.quantity = quantity.getText().toString();
                userProductModel.expiration_date = expireDate.getText().toString();
                userProductModel.completed = -1;
                userProductModel.longitude = longitude;
                userProductModel.latitude = latitude;
                postUserProduct(userProductModel);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();    //이미지 원본 경로
            productImage.setImageURI(imageUri);
        } else if (requestCode == QUALITY_SELECT && resultCode == RESULT_OK) {
            int quality = -1;
            if (data.hasExtra("quality")) quality = data.getIntExtra("quality", -1);
            qualityText.setText(Integer.toString(quality));
        }
    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();


        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


}

