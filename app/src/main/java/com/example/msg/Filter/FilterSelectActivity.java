package com.example.msg.Filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.msg.Api.GuideLineApi;
import com.example.msg.R;

import java.util.ArrayList;

public class FilterSelectActivity extends AppCompatActivity {

    //하위 액티비티로 넘겨줄 필터 객체.
    private FilterModel filterModel = new FilterModel();

    //레이아웃 관련 변수들
    private Button btnDistance;
    private Button btnCost;
    private Button btnQuantity;
    private Spinner smallSpinner;
    private Spinner bigSpinner;
    private SeekBar seekBarDistance;
    private SeekBar seekBarCost;
    private Button btnSubmit;
    private TextView textViewCost, textViewDistance;
    private RadioGroup radioGroup;
    private RadioButton radioHigh, radioMid, radioLow;

    //스피너 관련 변수
    private final ArrayList<String> smallCategories = new ArrayList<>();
    private ArrayAdapter<String> smallCategoriesAdapter;

    //플래그
    private int selectedFilter = 0;
    private boolean isShowingUserProduct;

    private void initializeLayout() {
        btnDistance = findViewById(R.id.filterSelectActivity_btn_distance);
        btnCost = findViewById(R.id.filterSelectActivity_btn_cost);
        btnQuantity = findViewById(R.id.filterSelectActivity_btn_quantity);
        bigSpinner = findViewById(R.id.filterSelectActivity_spinner_categoryA);
        smallSpinner = findViewById(R.id.filterSelectActivity_spinner_categoryB);
        seekBarDistance = findViewById(R.id.filterSelectActivity_seekbar_distance);
        seekBarCost = findViewById(R.id.filterSelectActivity_seekbar_cost);
        btnSubmit = findViewById(R.id.filterSelectActivity_btn_submit);
        textViewCost = findViewById(R.id.filterSelectActivity_textView_price);
        radioGroup = findViewById(R.id.filterSelectActivity_ratidgroup_guideline);
        radioHigh = findViewById(R.id.filterSelectActivity_ratidbtn_high);
        radioMid = findViewById(R.id.filterSelectActivity_ratidbtn_middle);
        radioLow = findViewById(R.id.filterSelectActivity_ratidbtn_low);
        textViewDistance = findViewById(R.id.filterSelectActivity_textView_distance);

        filterModel.setFilterTypeDistance();
        selectButtonFormChange(btnDistance);
        noSelectButtonFormChange(btnCost);
        noSelectButtonFormChange(btnQuantity);

        smallCategoriesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, smallCategories);
        smallSpinner.setAdapter(smallCategoriesAdapter);



    }

    public void disableLayoutIfUserProduct() {
        Intent intent = getIntent();
        isShowingUserProduct = intent.getBooleanExtra("isShowingUserProduct", false);

        if(isShowingUserProduct) {
            seekBarCost.setEnabled(false);
            disableButtonFormChange(btnCost);
            disableButtonFormChange(btnQuantity);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_select);

        initializeLayout();
        disableLayoutIfUserProduct();

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowingUserProduct) {
                    filterModel.setFilterTypeDistance();
                    selectButtonFormChange(btnDistance);
                    noSelectButtonFormChange(btnCost);
                    noSelectButtonFormChange(btnQuantity);
                }
            }
        });

        btnCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowingUserProduct) {
                    filterModel.setFilterTypePrice();
                    selectButtonFormChange(btnCost);
                    noSelectButtonFormChange(btnDistance);
                    noSelectButtonFormChange(btnQuantity);
                }
            }
        });

        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowingUserProduct) {
                    filterModel.setFilterTypeStock();
                    selectButtonFormChange(btnQuantity);
                    noSelectButtonFormChange(btnCost);
                    noSelectButtonFormChange(btnDistance);
                }
            }
        });

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

        smallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterModel.setCategory((String)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewDistance.setText(String.format("거리< %dm", progress));
                filterModel.setRange(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                sendFilterSetting();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.filterSelectActivity_ratidbtn_low) {
                    filterModel.setSearchLowQuality(true);
                    filterModel.setSearchMidQuality(false);
                    filterModel.setSearchHighQuality(false);
                } else if(checkedId == R.id.filterSelectActivity_ratidbtn_middle) {
                    filterModel.setSearchLowQuality(false);
                    filterModel.setSearchMidQuality(true);
                    filterModel.setSearchHighQuality(false);
                }
                else  {
                    filterModel.setSearchLowQuality(false);
                    filterModel.setSearchMidQuality(false);
                    filterModel.setSearchHighQuality(true);
                }
            }
        });

    }


    private void sendFilterSetting() {
        Intent intent = new Intent();
        intent.putExtra("Object", filterModel);
        setResult(RESULT_OK, intent);
        finish();
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

    private void disableButtonFormChange(Button button) {
        button.setBackgroundResource(R.drawable.bin_blue_disabled);
        button.setTextColor(Color.parseColor("#000000"));
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_baemin5), Typeface.NORMAL);
    }

}
