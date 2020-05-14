package com.example.msg.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.R;

import java.util.ArrayList;
import com.example.msg.DatabaseModel.RestaurantModel;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private ArrayList<RestaurantProductModel> arrayList;
    private Context context;

    public ProductsAdapter(ArrayList<RestaurantProductModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_item,parent,false);
        ProductsViewHolder holder=new ProductsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        String str = "";
        if(arrayList.get(position).fast == true) str = ",구독중인 식당입니다!";
        Glide.with(holder.itemView)
                .load(arrayList.get(position).p_imageURL)
                .into(holder.image);
        holder.title.setText(arrayList.get(position).title);
        holder.uid.setText(arrayList.get(position).cost + "원, " + arrayList.get(position).stock + "개" + str);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView uid;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image=itemView.findViewById(R.id.productList_imageView_Image);
            this.title=itemView.findViewById(R.id.productList_textView_title);
            this.uid=itemView.findViewById(R.id.productList_textView_uid);
        }
    }
}
