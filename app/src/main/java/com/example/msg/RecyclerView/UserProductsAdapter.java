package com.example.msg.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.Api.UserApi;
import com.example.msg.DatabaseModel.UserModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.msg.Sale.SaleUserActivity;


public class UserProductsAdapter extends RecyclerView.Adapter<UserProductsAdapter.ProductsViewHolder> {

    private ArrayList<UserProductModel> arrayList;
    private Context context;

    public UserProductsAdapter(ArrayList<UserProductModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userproduct_item,parent,false);
        ProductsViewHolder holder=new ProductsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position) {

        UserApi.getUserById(arrayList.get(position).user_id, new UserApi.MyCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                holder.name.setText(userModel.user_name);
                holder.grade.setRating(userModel.user_rating);
                holder.ban.setText("신고 횟수 "+userModel.ban_count+"회");
                //holder.dong.setText(arrayList.get(position).p_description); //이레 추가부탁 6월01일
                String addressString = null;
                Geocoder geocoder = new Geocoder(context, Locale.KOREAN);
//                Log.d("GOS", lat+" "+lng);
                try {
                    List<Address> addresses = geocoder.getFromLocation(userModel.latitude, userModel.longitude, 10);
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
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView dong;
        TextView name;
        RatingBar grade;
        TextView ban;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image=itemView.findViewById(R.id.userproduct_item_imageView_image);
            this.title=itemView.findViewById(R.id.userproduct_item_textView_title);
            this.dong=itemView.findViewById(R.id.userproduct_item_textView_dong);
            this.name=itemView.findViewById(R.id.userproduct_item_textView_name);
            this.grade=itemView.findViewById(R.id.userproduct_item_ratingBar_grade);
            this.ban=itemView.findViewById(R.id.userproduct_item_textView_ban);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        UserProductModel item = arrayList.get(pos);
                        //아이템을 얻는 부분.
                        Intent intent = new Intent(v.getContext(), SaleUserActivity.class);
                        intent.putExtra("Model", item);
                        v.getContext().startActivity(intent);


                    }
                }
            });
            //여기서 리사이클러뷰의 아이템이 클릭되는 것을 처리할 수 있음.
        }

    }


}
