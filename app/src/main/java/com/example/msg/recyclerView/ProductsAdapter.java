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
import com.example.msg.R;
import com.example.msg.fragment.HomeFragment;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private ArrayList<Products> arrayList;
    private Context context;

    public ProductsAdapter(ArrayList<Products> arrayList, Context context) {
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
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImageUrl())
                .into(holder.image);
        holder.title.setText(arrayList.get(position).getTitle());
        holder.uid.setText(arrayList.get(position).getUid());
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
