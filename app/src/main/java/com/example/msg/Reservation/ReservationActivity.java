package com.example.msg.Reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.Api.ReserveApi;

import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    Spinner spinner;

    String msg = "예약이 완료되었습니다.";
    String tmp;

    String[] categories = {"육류","어류","채소","향신료"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        button = (Button)findViewById(R.id.reservationActivity_button_item);
        editText = (EditText)findViewById(R.id.reservationActivity_edittext_item);
        spinner = (Spinner)findViewById(R.id.reservationActivity_spinner_category);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                tmp = categories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                ReserveModel reserveModel = new ReserveModel();
                reserveModel.user_id = uid;
                reserveModel.category = tmp;
                reserveModel.keyword = editText.getText().toString();
                reserveModel.time = makeCurrentTimeString();

                ReserveApi.postReservation(reserveModel, new ReserveApi.MyCallback() {
                    @Override
                    public void onSuccess(ReserveModel reserveModel) {
                        FirebaseMessaging.getInstance().subscribeToTopic(reserveModel.keyword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //String msg = getString(R.string.msg_subscribed);
                                        if (!task.isSuccessful()) {
                                            //  msg = getString(R.string.msg_subscribe_failed);
                                        }
                                        //Log.d(TAG, msg);
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });


            }
        });
        //예약과 알림 기능 -> Reservations 컬렉션에 예약정보 입력, 예약 키워드를 구독하여 Firebase server에서 새로운 상품이 입력될때마다 트리거 발생
        //트리거 발생시 Products 컬렉션과 Reservations 컬렉션의 keyword를 쿼리하여 있으면 푸시 알림 전송


    }
    private static String makeCurrentTimeString() {
        Date now = new Date();
        DateFormat timeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return timeFormat.format(now);
    }
}
