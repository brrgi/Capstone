package com.example.msg.UserFragment;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.EditProfile.EditProfileActivity;
import com.example.msg.Map.DaumWebViewActivity;
import com.example.msg.Map.MapActivity;
import com.example.msg.R;
import com.example.msg.Sale.PurchaseHistoryActivity;
import com.example.msg.Sale.SalesHistoryActivity;
import com.example.msg.Subscription.SubscriptionActivity;
import com.example.msg.menu.HelpActivity;
import com.example.msg.menu.NoticeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AccountFragment extends Fragment {
    private View view;
    private LinearLayout saleshistory;
    private LinearLayout purchasehistory;
    private LinearLayout subscribehistory;
    private TextView txt_username;
    private TextView txt_user_rating;
    private double defaultLongitude = 0;
    private double defaultLatitude = 0;
    private String dong="";
    private TextView address;
    private Button editProfile;
    private Button map, help, notice;

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
        view=inflater.inflate(R.layout.fragment_account,container,false);
        saleshistory=view.findViewById(R.id.account_linearLayout_saleshistory);
        purchasehistory=view.findViewById(R.id.account_linearLayout_purchasehistory);
        subscribehistory=view.findViewById(R.id.account_linearLayout_subscribehistory);
        address=(TextView)view.findViewById(R.id.account_textView_address);
        txt_user_rating = view.findViewById(R.id.account_textView_rating);
        txt_username = view.findViewById(R.id.account_textView_UID);
        editProfile = view.findViewById(R.id.account_textView_editprofile);
        help= view.findViewById(R.id.account_button_help);
        notice= view.findViewById(R.id.account_button_notice);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        getAddress(uid);

//    UserApi
        getLocation(defaultLatitude,defaultLongitude);

        UserApi.getUserById(AuthenticationApi.getCurrentUid(), new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                txt_user_rating.setText("평점·마일리지 : "+ userModel.user_rating+" · "+userModel.mileage);
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

        subscribehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });


        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NoticeActivity.class));
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
