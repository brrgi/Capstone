package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.Domain.UserProductApi;

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

public class PayActivity extends AppCompatActivity {

    private View v;
    private int stuck = 10;
    private Button gotoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        BootpayAnalytics.init(this, "5eb302fa02f57e001f1ede7e");
        onClick_request(v);

    }

    public void onClick_request(View v) {
        // 결제호출

        UserProductApi.getProduct(new UserProductApi.MyCallback() {
            @Override
            public void onCallback(UserProductModel userProductModel) {
                BootUser bootUser = new BootUser().setPhone("010-1234-5678");
                BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                Bootpay.init(getFragmentManager())
                        .setApplicationId("5eb302fa02f57e001f1ede7e") // 해당 프로젝트(안드로이드)의 application id 값
                        .setContext(getApplicationContext())
                        .setBootUser(bootUser)
                        .setBootExtra(bootExtra)
                        .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                        .setName(userProductModel.title) // 결제할 상품명
                        .setOrderId("1234") // 결제 고유번호 expire_month
                        .setPrice(3000) // 결제할 금액
                        .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            @Override
                            public void onConfirm(@Nullable String message) {

                                if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                                else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                Log.d("confirm", message);
                            }
                        })
                        .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                            @Override
                            public void onDone(@Nullable String message) {
                                Log.d("done", message);
                            }
                        })
                        .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                            @Override
                            public void onReady(@Nullable String message) {
                                Log.d("ready", message);
                            }
                        })
                        .onCancel(new CancelListener() { // 결제 취소시 호출
                            @Override
                            public void onCancel(@Nullable String message) {
                                Log.d("cancel", message);
                                finish();
                            }
                        })
                        .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                            @Override
                            public void onError(@Nullable String message) {
                                Log.d("error", message);
                                finish();
                            }
                        })
                        .onClose(
                                new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", "close");
                                        finish();
                                    }
                                })
                        .request();
            }
        }, "1rdx4BsFHYHqtztzrPYb");


    }
}
