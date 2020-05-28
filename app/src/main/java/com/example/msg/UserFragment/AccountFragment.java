package com.example.msg.UserFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msg.Domain.AuthenticationApi;
import com.example.msg.Domain.UserApi;
import com.example.msg.Domain.UserProductApi;
import com.example.msg.R;
import com.example.msg.SaleActivity;
import com.example.msg.Sale.SalesHistoryActivity;
import com.example.msg.recyclerView.ResProductsAdapter;
import com.example.msg.recyclerView.UserProductsAdapter;


public class AccountFragment extends Fragment {
    private View view;
    private LinearLayout saleshistory;
    private LinearLayout purchasehistory;
    private LinearLayout subscribehistory;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_account,container,false);
        saleshistory=view.findViewById(R.id.account_linearLayout_saleshistory);
        purchasehistory=view.findViewById(R.id.account_linearLayout_purchasehistory);
        subscribehistory=view.findViewById(R.id.account_linearLayout_subscribehistory);

        saleshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SalesHistoryActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
