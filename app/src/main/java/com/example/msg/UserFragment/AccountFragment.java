package com.example.msg.UserFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.R;
import com.example.msg.Sale.PurchaseHistoryActivity;
import com.example.msg.Sale.SalesHistoryActivity;


public class AccountFragment extends Fragment {
    private View view;
    private LinearLayout saleshistory;
    private LinearLayout purchasehistory;
    private LinearLayout subscribehistory;

    private TextView txt_username;
    private TextView txt_user_rating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_account,container,false);
        saleshistory=view.findViewById(R.id.account_linearLayout_saleshistory);
        purchasehistory=view.findViewById(R.id.account_linearLayout_purchasehistory);
        subscribehistory=view.findViewById(R.id.account_linearLayout_subscribehistory);

        txt_user_rating = view.findViewById(R.id.account_textView_rating);
        txt_username = view.findViewById(R.id.account_textView_UID);

        UserApi.getUserById(AuthenticationApi.getCurrentUid(), new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                txt_user_rating.setText("내 평점 : "+ userModel.user_rating);
                txt_username.setText(userModel.user_name);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        saleshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SalesHistoryActivity.class);
                startActivity(intent);
            }
        });

        purchasehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseHistoryActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
