package com.example.msg.ResFragment;


import android.graphics.Color;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.text.Html;
        import android.text.SpannableString;
        import android.text.Spanned;
        import android.text.style.ForegroundColorSpan;
        import android.text.style.StyleSpan;
        import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.StatisticsApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ResAccountBookFragment extends Fragment {
    private View view;
    private TextView date;
    private TextView title;
    private TextView yesterday;
    private TextView today;
    private TextView yesterdayCost;
    private TextView todayCost;
    private TextView monthCost;
    private TextView totalCost;


    private int total1, total2, total3, total4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resaccountbook,container,false);

        date=(TextView)view.findViewById(R.id.resaccountbook_textView_date);
        title=(TextView)view.findViewById(R.id.resaccountbook_textView_title);
        yesterday=(TextView)view.findViewById(R.id.resaccountbook_textView_yesterdayDate);
        today=(TextView) view.findViewById(R.id.resaccountbook_textView_todayDate);
        yesterdayCost=(TextView) view.findViewById(R.id.resaccountbook_textView_yesterdayCost);
        todayCost=(TextView)view.findViewById(R.id.resaccountbook_textView_todayCost);
        monthCost=(TextView)view.findViewById(R.id.resaccountbook_textView_monthCost);
        totalCost=(TextView)view.findViewById(R.id.resaccountbook_textView_totalCost);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        RestaurantApi.getUserById(uid, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                title.setText(restaurantModel.res_name);
                long now = System.currentTimeMillis();
                Date dates = new Date(now);
                SimpleDateFormat sdfNow1 = new SimpleDateFormat("yyyy-MM-dd E"+"요일");
                SimpleDateFormat todayss = new SimpleDateFormat("MM/dd");
                SimpleDateFormat yesters = new SimpleDateFormat("MM/dd");

                SimpleDateFormat todayYear = new SimpleDateFormat("yy");
                SimpleDateFormat todayMonth = new SimpleDateFormat("MM");
                SimpleDateFormat todayDay = new SimpleDateFormat("dd");

                Calendar cal=Calendar.getInstance();
                cal.add(cal.DATE,-1);

                String formatDate = sdfNow1.format(dates);
                String todays=todayss.format(dates);
                String yester=yesters.format(cal.getTime());

                String todayDateYear = todayYear.format(dates);
                String todayDateMonth = todayMonth.format(dates);
                String todayDateDay = todayDay.format(dates);

                String yesterDateYear = todayYear.format(cal.getTime());
                String yesterDateMonth = todayMonth.format(cal.getTime());
                String yesterDateDay = todayDay.format(cal.getTime());

                date.setText(formatDate);
                yesterday.setText(yester);
                today.setText(todays);


                StatisticsApi.getYesterdayCost(uid, 1, Integer.parseInt(yesterDateYear, 10), Integer.parseInt(yesterDateMonth, 10), Integer.parseInt(yesterDateDay, 10), new StatisticsApi.MyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Integer> sum) {
                        total1=0;
                        for (int i=0; i<sum.size(); i++){
                            total1+=sum.get(i);
                        }
                        Log.d("뿌슝1",Integer.toString(total1)+" "+sum);
                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });

                StatisticsApi.getTodayCost(uid, 1, Integer.parseInt(todayDateYear, 10), Integer.parseInt(todayDateMonth, 10), Integer.parseInt(todayDateDay, 10), new StatisticsApi.MyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Integer> sum) {
                        total2=0;
                        for (int i=0; i<sum.size(); i++){
                            total2+=sum.get(i);
                        }
                        Log.d("뿌슝2",Integer.toString(total2)+" "+sum);

                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });

                StatisticsApi.getMonthCost(uid, 1, Integer.parseInt(todayDateYear, 10), Integer.parseInt(todayDateMonth, 10), new StatisticsApi.MyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Integer> sum) {
                        total3=0;
                        for (int i=0; i<sum.size(); i++){
                            total3+=sum.get(i);
                        }
                        Log.d("뿌슝3",Integer.toString(total3)+" "+sum);

                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });

                StatisticsApi.getTotalCost(uid, 1, new StatisticsApi.MyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Integer> sum) {
                        total4=0;
                        for (int i=0; i<sum.size(); i++){
                            total4+=sum.get(i);
                        }
                        Log.d("뿌슝4",Integer.toString(total4)+" "+sum);

                    }

                    @Override
                    public void onFail(int errorCode, Exception e) {

                    }
                });

                yesterdayCost.setText(total1+"원");
                todayCost.setText(total2+"원");
                monthCost.setText(total3+"원");
                totalCost.setText(total4+"원");
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        SpannableString content = new SpannableString(date.getText().toString());









// 저는이미 TextView 에 String 을 넣었기 때문에 위와 같이 TextView.getText().toString() 했음

        content.setSpan(new UnderlineSpan(), 0, content.length(),0);
        content.setSpan(new StyleSpan(Typeface.ITALIC), 0, content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        date.setText(content);

        BarChart mBarChart = (BarChart) view.findViewById(R.id.barchart);

        mBarChart.addBar(new BarModel(2.3f, 0xFF123456));
        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart.addBar(new BarModel(3.3f, 0xFF563456));
        mBarChart.addBar(new BarModel(1.1f, 0xFF873F56));
        mBarChart.addBar(new BarModel(2.7f, 0xFF56B7F1));
        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(4.f,  0xFF1BA4E6));

        mBarChart.startAnimation();

        ///
        StackedBarChart mStackedBarChart = (StackedBarChart) view.findViewById(R.id.stackedbarchart);

        StackedBarModel s1 = new StackedBarModel("12.4");

        s1.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s1.addBar(new BarModel(2.3f, 0xFF56B7F1));
        s1.addBar(new BarModel(2.3f, 0xFFCDA67F));

        StackedBarModel s2 = new StackedBarModel("13.4");
        s2.addBar(new BarModel(1.1f, 0xFF63CBB0));
        s2.addBar(new BarModel(2.7f, 0xFF56B7F1));
        s2.addBar(new BarModel(0.7f, 0xFFCDA67F));

        StackedBarModel s3 = new StackedBarModel("14.4");

        s3.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s3.addBar(new BarModel(2.f, 0xFF56B7F1));
        s3.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s4 = new StackedBarModel("15.4");
        s4.addBar(new BarModel(1.f, 0xFF63CBB0));
        s4.addBar(new BarModel(4.2f, 0xFF56B7F1));
        s4.addBar(new BarModel(2.1f, 0xFFCDA67F));

        mStackedBarChart.addBar(s1);
        mStackedBarChart.addBar(s2);
        mStackedBarChart.addBar(s3);
        mStackedBarChart.addBar(s4);

        mStackedBarChart.startAnimation();

        ////
        PieChart mPieChart = (PieChart) view.findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();
        ////
        ValueLineChart mCubicValueLineChart = (ValueLineChart) view.findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("5/23", 2.4f));
        series.addPoint(new ValueLinePoint("5/24", 3.4f));
        series.addPoint(new ValueLinePoint("5/25", .4f));
        series.addPoint(new ValueLinePoint("5/26", 1.2f));
        series.addPoint(new ValueLinePoint("5/27", 2.6f));
        series.addPoint(new ValueLinePoint("5/28", 1.0f));
        series.addPoint(new ValueLinePoint("5/29", 3.5f));
        series.addPoint(new ValueLinePoint("5/30", 2.4f));
        series.addPoint(new ValueLinePoint("5/31", 2.4f));
        series.addPoint(new ValueLinePoint("6/1", 3.4f));
        series.addPoint(new ValueLinePoint("6/2", 3.0f));
        series.addPoint(new ValueLinePoint("6/3", 2.5f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
        return view;




    }


}
