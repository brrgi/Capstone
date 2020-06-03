package com.example.msg.ResFragment;


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
import com.example.msg.Api.RestaurantApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.R;
import com.example.msg.Sale.ResSalesHistoryActivity;


public class ResAccountFragment extends Fragment {
    private View view;
    private LinearLayout ressaleshistory;

    private TextView res_name;
    private TextView res_address;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resaccount,container,false);
        ressaleshistory=view.findViewById(R.id.resaccount_linearLayout_saleshistory);

        res_name =view.findViewById(R.id.resaccount_textView_UID);
        res_address=view.findViewById(R.id.resaccount_textView_address);

        RestaurantApi.getUserById(AuthenticationApi.getCurrentUid(), new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                res_name.setText(restaurantModel.res_name);
                res_address.setText(restaurantModel.res_address+" "+restaurantModel.res_address_detail);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        ressaleshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResSalesHistoryActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
