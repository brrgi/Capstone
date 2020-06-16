package com.example.msg.saleFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.R;

public class ResInfoFragment extends Fragment {
    private View view;
    private static final String TAG = "ProductInfo";

    private ImageView profile;
    private TextView name;
    private TextView phone;
    private TextView pickupstarttime;
    private TextView pickupendtime;
    private TextView address;
    private TextView description;

    private String res_id;

    private void initializeLayout(final Context context) {
        profile = view.findViewById(R.id.ResInfoFragment_imageview_profile);
        name = view.findViewById(R.id.ResInfoFragment_textView_name);
        phone =  view.findViewById(R.id.ResInfoFragment_textView_phone);
        pickupstarttime = view.findViewById(R.id.ResInfoFragment_textView_pickupstarttime);
        pickupendtime = view.findViewById(R.id.ResInfoFragment_textView_pickupendtime);
        address=view.findViewById((R.id.ResInfoFragment_textView_address));
        description=view.findViewById((R.id.ResInfoFragment_textView_description));

        Bundle bundle=getArguments();
        if(bundle !=null) {
            res_id = bundle.getString("res_id");
        }

        RestaurantApi.getUserById(res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                Glide.with(ResInfoFragment.this).load(restaurantModel.res_imageURL).into(profile);
                name.setText(restaurantModel.res_name);
                phone.setText(restaurantModel.res_phone);
                pickupstarttime.setText(restaurantModel.pickup_start_time);
                pickupendtime.setText(restaurantModel.pickup_end_time);
                address.setText(restaurantModel.res_address);
                description.setText(restaurantModel.res_description);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resinfo,container,false);
        initializeLayout(getContext());

        return view;

    }
}
