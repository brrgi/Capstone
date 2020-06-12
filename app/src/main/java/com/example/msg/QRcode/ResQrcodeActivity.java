package com.example.msg.QRcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class ResQrcodeActivity extends AppCompatActivity {
    private ImageView Qrimage;
    private String text;
    private static final String TAG = "ResQrcodeActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resqrcode);

        Qrimage=(ImageView)findViewById(R.id.resqrcode_imageView_qrcode);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        Log.d(TAG,"uid "+uid);
        UserApi.getUserById(uid, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                Intent intent=getIntent();
                final Object obj = (Object)intent.getSerializableExtra("Model");
                RestaurantProductModel restaurantProductModel = (RestaurantProductModel)obj;
                Log.d(TAG,"user_name "+userModel.user_name+" "+userModel.user_name.getClass().getName());
                Log.d(TAG,"categorySmall "+ ((RestaurantProductModel) obj).categorySmall+" "+((RestaurantProductModel) obj).categorySmall.getClass().getName());
                text=userModel.user_name+"은 "+ ((RestaurantProductModel) obj).categorySmall + "을 수령합니다!";
                Log.d(TAG,"text "+text+ " "+ text.getClass().getName());

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    Hashtable hints = new Hashtable();
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200,hints);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Qrimage.setImageBitmap(bitmap);
                }catch (Exception e){}

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });


    }
}
