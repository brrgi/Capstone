package com.example.msg.saleFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msg.Api.RestaurantProductApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.R;

public class ProductInfoFragment extends Fragment {
    private View view;
    private static final String TAG = "ProductInfo";

    private TextView txt_category;
    private TextView txt_quantity;
    private TextView txt_quality;
    private TextView txt_expireDate;
    private TextView txt_description;
    private TextView txt_cost;

    private String rproduct_id;
    private double latitude;

    private void initializeLayout(final Context context) {
        txt_category = view.findViewById(R.id.productInfo_textView_categoryBig);
        txt_description = view.findViewById(R.id.productInfo_textView_description);
        txt_expireDate =  view.findViewById(R.id.productInfo_textView_expiredDate);
        txt_quality = view.findViewById(R.id.productInfo_textView_quality);
        txt_quantity = view.findViewById(R.id.productInfo_textView_quantity);
        txt_cost=view.findViewById((R.id.productInfo_textView_cost));

        Bundle bundle=getArguments();
        if(bundle !=null) {
            rproduct_id = bundle.getString("rproduct_id");
        }

        RestaurantProductApi.getProduct(rproduct_id, new RestaurantProductApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantProductModel restaurantProductModel) {
                txt_category.setText(restaurantProductModel.categoryBig + " -> " + restaurantProductModel.categorySmall);
                //txt_salesman.setText("판매자 : "+r_name); //더미 테스트라 아직 받아오지 못함 getRestaurant로 받아와야 할 예정
                txt_quantity.setText(restaurantProductModel.quantity);
                if (restaurantProductModel.quality==1){
                    txt_quality.setText("하");
                }
                else if (restaurantProductModel.quality==2){
                    txt_quality.setText("중");
                }
                else if (restaurantProductModel.quality==3){
                    txt_quality.setText("상");
                }
                txt_expireDate.setText(restaurantProductModel.expiration_date);
                txt_description.setText(restaurantProductModel.p_description);
                String c=Integer.toString(restaurantProductModel.cost);

                latitude=restaurantProductModel.latitude;

                txt_cost.setText(c);
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_productinfo,container,false);
        initializeLayout(getContext());

        return view;

    }
}
