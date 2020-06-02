package com.example.msg.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.Api.RestaurantApi;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.msg.Sale.SaleActivity;


public class ResProductsAdapter extends RecyclerView.Adapter<ResProductsAdapter.ProductsViewHolder> {

    private ArrayList<RestaurantProductModel> arrayList;
    private Context context;

    public ResProductsAdapter(ArrayList<RestaurantProductModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resproduct_item,parent,false);
        ProductsViewHolder holder=new ProductsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, int position) {

        RestaurantApi.getUserById(arrayList.get(position).res_id, new RestaurantApi.MyCallback() {
            @Override
            public void onSuccess(RestaurantModel restaurantModel) {
                holder.name.setText(restaurantModel.res_name);
                holder.grade.setRating(restaurantModel.res_rating);
                String addressString = null;
                Geocoder geocoder = new Geocoder(context, Locale.KOREAN);
//                Log.d("GOS", lat+" "+lng);
                try {
                    List<Address> addresses = geocoder.getFromLocation(restaurantModel.res_latitude, restaurantModel.res_longitude, 10);
                    for (int i=0; i<addresses.size(); i++) {
                        if(addresses.get(i).getThoroughfare() != null ) {
                            holder.dong.setText(addresses.get(i).getThoroughfare());
                        }
//                    Log.d("GOS", addresses.get(i).getThoroughfare());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });

        Glide.with(holder.itemView)
                .load(arrayList.get(position).p_imageURL)
                .into(holder.image);
        holder.title.setText(arrayList.get(position).title);

        holder.cost.setText(arrayList.get(position).cost + "원");
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView cost;
        TextView name;
        RatingBar grade;
        TextView dong;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image=itemView.findViewById(R.id.resproduct_item_imageView_image);
            this.title=itemView.findViewById(R.id.resproduct_item_textView_title);
            this.cost=itemView.findViewById(R.id.resproduct_item_textView_cost);
            this.grade=itemView.findViewById(R.id.resproduct_item_ratingBar_grade);
            this.name=itemView.findViewById(R.id.resproduct_item_textView_name);
            this.dong=itemView.findViewById(R.id.resproduct_item_textView_dong);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        RestaurantProductModel item = arrayList.get(pos);
                        //아이템을 얻는 부분.
                        Intent intent = new Intent(v.getContext(), SaleActivity.class);
                        intent.putExtra("Model", item);
                        v.getContext().startActivity(intent);


                    }
                }
            });
            //여기서 리사이클러뷰의 아이템이 클릭되는 것을 처리할 수 있음.
        }

    }
}
