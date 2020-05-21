package com.example.msg.recyclerView;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.RadialGradient;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.msg.DatabaseModel.GuideLineModel;
import com.example.msg.Domain.GuideLineApi;
import com.example.msg.R;

public class QualitySelectActivity extends AppCompatActivity {

    private TextView highText;
    private TextView midText;
    private TextView lowText;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_select);
        highText = (TextView)findViewById(R.id.quality_select_textView_high);
        midText = (TextView)findViewById(R.id.quality_select_textView_mid);
        lowText = (TextView)findViewById(R.id.quality_select_textView_low);
        cancel = (Button)findViewById(R.id.quality_select_button_cancel);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
