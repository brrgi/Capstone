package com.example.msg.RecyclerView;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.Api.ReserveApi;
import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionsViewHolder> {

    private ArrayList<RestaurantModel> arrayList;
    private Context context;

    public SubscriptionsAdapter(ArrayList<RestaurantModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item,parent,false);
        SubscriptionsViewHolder holder = new SubscriptionsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionsViewHolder holder, int position) {

        holder.res_name.setText(arrayList.get(position).res_name);
        holder.res_address.setText(arrayList.get(position).res_address);
        holder.description.setText(arrayList.get(position).res_description);
        holder.ratingBar.setRating(arrayList.get(position).res_rating);
        Glide.with(holder.itemView)
                .load(arrayList.get(position).res_imageURL)
                .into(holder.resImage);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class SubscriptionsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView res_name;
        ImageView resImage;
        TextView description;
        TextView res_address;
        RatingBar ratingBar;

        public SubscriptionsViewHolder(@NonNull View view) {
            super(view);
            this.res_address = view.findViewById(R.id.subscription_item_textView_address);
            this.resImage = view.findViewById(R.id.subscription_item_imageView_image);
            this.description = view.findViewById(R.id.subscription_item_textView_description);
            this.res_name = view.findViewById(R.id.subscription_item_textView_name);
            this.ratingBar = view.findViewById(R.id.subscription_item_ratingBar_grade);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem deleteItem = contextMenu.add(Menu.NONE,1001,1,"구독 취소");
            deleteItem.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case 1001:

                        break;
                }
                return true;
            }
        };
    }
}

