package com.example.msg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.msg.DataModel.FilterModel;

public class FilterSelectActivity extends AppCompatActivity {

    //하위 액티비티로 넘겨줄 필터 객체.
    private FilterModel filterModel = new FilterModel();

    //레이아웃 관련 변수들
    private Button btnDistance;
    private Button btnCost;
    private Button btnQuantity;
    private Spinner categorySmall;
    private Spinner categoryBig;
    private SeekBar seekBarDistance;
    private SeekBar seekBarCost;
    private Button btnSubmit;
    private TextView textViewCost;

    //플래그
    private int selectedFilter = 0;

    private void initializeLayout() {
        btnDistance = findViewById(R.id.filterSelectActivity_btn_distance);
        btnCost = findViewById(R.id.filterSelectActivity_btn_cost);
        btnQuantity = findViewById(R.id.filterSelectActivity_btn_quantity);
        categoryBig = findViewById(R.id.filterSelectActivity_spinner_categoryA);
        categorySmall = findViewById(R.id.filterSelectActivity_spinner_categoryB);
        seekBarDistance = findViewById(R.id.filterSelectActivity_seekbar_distance);
        seekBarCost = findViewById(R.id.filterSelectActivity_seekbar_cost);
        btnSubmit = findViewById(R.id.filterSelectActivity_btn_submit);
        textViewCost = findViewById(R.id.filterSelectActivity_textView_price);

        filterModel.setFilterTypeDistance();
        selectButtonFormChange(btnDistance);
        noSelectButtonFormChange(btnCost);
        noSelectButtonFormChange(btnQuantity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_select);

        initializeLayout();

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterModel.setFilterTypeDistance();
                selectButtonFormChange(btnDistance);
                noSelectButtonFormChange(btnCost);
                noSelectButtonFormChange(btnQuantity);
            }
        });

        btnCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterModel.setFilterTypePrice();
                selectButtonFormChange(btnCost);
                noSelectButtonFormChange(btnDistance);
                noSelectButtonFormChange(btnQuantity);
            }
        });

        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterModel.setFilterTypeStock();
                selectButtonFormChange(btnQuantity);
                noSelectButtonFormChange(btnCost);
                noSelectButtonFormChange(btnDistance);
            }
        });

        seekBarCost.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewCost.setText(String.format("가격< %d", progress));
                filterModel.setPrice(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Object", filterModel);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }


    private void sendFilterSetting() {
    }

    private void selectButtonFormChange(Button button) {
        button.setBackgroundResource(R.drawable.bin_blue);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_baemin5), Typeface.BOLD);
    }

    private void noSelectButtonFormChange(Button button) {
        button.setBackgroundResource(R.drawable.bin_blue_none_select);
        button.setTextColor(Color.parseColor("#000000"));
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_baemin5), Typeface.NORMAL);
    }
}
