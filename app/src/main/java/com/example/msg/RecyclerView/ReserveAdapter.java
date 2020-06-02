package com.example.msg.RecyclerView;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msg.Api.ReserveApi;
import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.example.msg.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ReservesViewHolder> {

    private ArrayList<ReserveModel> arrayList;
    private Context context;

    public ReserveAdapter(ArrayList<ReserveModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_list,parent,false);
        ReservesViewHolder holder = new ReservesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservesViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).keyword);
        holder.time.setText(arrayList.get(position).time);
        holder.category.setText(arrayList.get(position).category);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ReservesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView title;
        TextView category;
        TextView time;
        public ReservesViewHolder(@NonNull View view) {
            super(view);
            this.category = view.findViewById(R.id.reservation_textView_category);
            this.title = view.findViewById(R.id.reservation_textView_ID);
            this.time = view.findViewById(R.id.reservation_textView_time);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem deleteItem = contextMenu.add(Menu.NONE,1001,1,"삭제");
            deleteItem.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case 1001:
                        ReserveApi.deleteReservationByReserveId(arrayList.get(getAdapterPosition()).reservation_id, new ReserveApi.MyCallback() {
                            @Override
                            public void onSuccess(ReserveModel reserveModel) {
                                arrayList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(),arrayList.size());
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(arrayList.get(getAdapterPosition()).keyword);
                            }
                            @Override
                            public void onFail(int errorCode, Exception e) {
                            }
                        });
                        break;
                }
                return true;
            }
        };
    }
}

