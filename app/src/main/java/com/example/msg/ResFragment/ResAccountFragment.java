package com.example.msg.ResFragment;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.R;
import com.example.msg.Sale.ResSalesHistoryActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ResAccountFragment extends Fragment {
    private View view;
    private LinearLayout ressaleshistory;

    private String dong="";
    private TextView address;
    private double defaultLongitude = 0;
    private double defaultLatitude = 0;

    private void getAddress(String uid){
        UserApi.getUserById(uid, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                defaultLatitude=userModel.latitude;
                defaultLongitude=userModel.longitude;
                //Toast.makeText(getActivity(), defaultLatitude+" "+defaultLongitude, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resaccount,container,false);
        ressaleshistory=view.findViewById(R.id.resaccount_linearLayout_saleshistory);



        ressaleshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResSalesHistoryActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    public void getLocation(double lat, double lng){
        String addressString = null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.KOREAN);
        Log.d("GOS", lat+" "+lng);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 10);
            for (int i=0; i<addresses.size(); i++) {
                if(addresses.get(i).getThoroughfare() != null ) {
                    dong = addresses.get(i).getThoroughfare();
                    address.setText(dong);
                }
//                    Log.d("GOS", addresses.get(i).getThoroughfare());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
