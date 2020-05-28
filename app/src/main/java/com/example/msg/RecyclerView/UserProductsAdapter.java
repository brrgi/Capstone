package com.example.msg.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).p_imageURL)
                .into(holder.image);
        holder.title.setText(arrayList.get(position).title);
        holder.uid.setText(arrayList.get(position).p_description);
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
            this.image=itemView.findViewById(R.id.userproduct_item_imageView_image);
            this.title=itemView.findViewById(R.id.userproduct_item_textView_title);
            this.uid=itemView.findViewById(R.id.userproduct_item_textView_uid);

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
