package com.example.msg.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.msg.DatabaseModel.GuideLineModel;
import com.example.msg.Api.GuideLineApi;
import com.example.msg.R;

public class QualitySelectActivity extends AppCompatActivity {

    private TextView highText;
    private TextView midText;
    private TextView lowText;
    private Button cancel;
    private Button submit;
    private RadioButton high, mid, low;
    private int checkedValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_select);
        highText = (TextView)findViewById(R.id.quality_select_textView_high);
        midText = (TextView)findViewById(R.id.quality_select_textView_mid);
        lowText = (TextView)findViewById(R.id.quality_select_textView_low);
        cancel = (Button)findViewById(R.id.quality_select_button_cancel);
        submit = (Button)findViewById(R.id.quality_select_button_submit);
        high = (RadioButton)findViewById(R.id.quality_select_radioButton_high);
        mid = (RadioButton)findViewById(R.id.quality_select_radioButton_mid);
        low = (RadioButton)findViewById(R.id.quality_select_radioButton_low);

        String category = getIntent().getExtras().getString("category");


        GuideLineApi.getGuideByName(category, new GuideLineApi.MyCallback() {
            @Override
            public void onSuccess(GuideLineModel guideLineModel) {
                highText.setText(guideLineModel.spec1);
                midText.setText(guideLineModel.spec2);
                lowText.setText(guideLineModel.spec3);
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                //모델을 불러오는데 실패함.
            }
        });


        high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    mid.setChecked(false);
                    low.setChecked(false);
                    checkedValue = 3;
                }
            }
        });

        mid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    high.setChecked(false);
                    low.setChecked(false);
                    checkedValue = 2;
                }
            }
        });

        low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    mid.setChecked(false);
                    high.setChecked(false);
                    checkedValue = 1;
                }
            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quality", -1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quality", checkedValue);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


}
