package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class SaleActivity extends AppCompatActivity {

    private int stuck = 10;

    private Button btn_chat;
    private Button btn_buy;
    private TextView txt_title;
    private TextView txt_category;
    private TextView txt_quantity;
    private TextView txt_quality;
    private TextView txt_expireDate;
    private TextView txt_description;
    private TextView txt_salesman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        txt_category = (TextView) findViewById(R.id.saleActivity_textView_categoryBig);
        txt_description = (TextView) findViewById(R.id.saleActivity_textView_description);
        txt_expireDate = (TextView) findViewById(R.id.saleActivity_textView_expiredDate);
        txt_quality = (TextView) findViewById(R.id.saleActivity_textView_quality);
        txt_quantity = (TextView) findViewById(R.id.saleActivity_textView_quantity);
        txt_salesman = (TextView) findViewById(R.id.saleActivity_textView_salesman);
        txt_title = (TextView) findViewById(R.id.saleActivity_textView_title);
        btn_buy = (Button) findViewById(R.id.saleActivity_button_buy);
        btn_chat = (Button) findViewById(R.id.saleActivity_button_chat);

        UserProductApi.getProduct(new UserProductApi.MyCallback() {
            @Override
            public void onCallback(UserProductModel userProductModel) {
                Log.d("kyudong", "여기까진 실행됨.");
                txt_title.setText("제목 : " + userProductModel.title);
                Log.d("kyudong", userProductModel.title);
                txt_category.setText("카테고리 : " + userProductModel.categoryBig + " -> " + userProductModel.categorySmall);
                txt_salesman.setText("판매자");
                txt_quantity.setText("양 : " + userProductModel.quantity);
                txt_quality.setText("품질 : " + userProductModel.quality);
                txt_expireDate.setText("유통기한 : " + userProductModel.expiration_date);
                txt_description.setText("상세설명 : " + userProductModel.p_description);
            }
        }, "1rdx4BsFHYHqtztzrPYb");


        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
    }


}

