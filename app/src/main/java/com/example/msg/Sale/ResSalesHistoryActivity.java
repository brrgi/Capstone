package com.example.msg.Sale;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.msg.R;
import com.example.msg.accountFragment.CompletedSaleResFagment;
import com.example.msg.accountFragment.OnSaleResFagment;
import com.example.msg.accountFragment.TradingResFagment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ResSalesHistoryActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private OnSaleResFagment onSaleResFagment;
    private CompletedSaleResFagment completedSaleResFagment;
    private TradingResFagment tradingResFagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        viewPager=findViewById(R.id.saleshistory_viewpager);
        tabLayout=findViewById(R.id.saleshistory_tablayout);

        onSaleResFagment=new OnSaleResFagment();
        tradingResFagment=new TradingResFagment();
        completedSaleResFagment=new CompletedSaleResFagment();


        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(onSaleResFagment,"판매중");
        viewPagerAdapter.addFragment(tradingResFagment,"거래중");
        viewPagerAdapter.addFragment(completedSaleResFagment,"판매완료");
        viewPager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments=new ArrayList<>();
        private List<String> fragmentTitle=new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
