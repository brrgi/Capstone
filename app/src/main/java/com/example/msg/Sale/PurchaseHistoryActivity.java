package com.example.msg.Sale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.msg.R;
import com.example.msg.accountFragment.CompletedPurchaseFragment;
import com.example.msg.accountFragment.CompletedSaleFagment;
import com.example.msg.accountFragment.OnPurchaseFragment;
import com.example.msg.accountFragment.OnSaleFagment;
import com.example.msg.accountFragment.TradingFagment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private OnPurchaseFragment onPurchaseFragment;
    private CompletedPurchaseFragment completedPurchaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        viewPager=findViewById(R.id.purchasehistory_viewpager);
        tabLayout=findViewById(R.id.purchasehistory_tablayout);

        onPurchaseFragment = new OnPurchaseFragment();
        completedPurchaseFragment = new CompletedPurchaseFragment();

        tabLayout.setupWithViewPager(viewPager);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(onPurchaseFragment,"구매중");
        viewPagerAdapter.addFragment(completedPurchaseFragment,"구매완료");
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
