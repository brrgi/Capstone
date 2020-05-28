package com.example.msg.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.R;

import java.util.ArrayList;

public class ChatDataAdapter extends RecyclerView.Adapter<ChatDataAdapter.ChatDataViewHolder> {
    private ArrayList<ChatData> arrayList;
    private Context context;

    public ChatDataAdapter(ArrayList<ChatData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item,parent,false);
        ChatDataAdapter.ChatDataViewHolder holder=new ChatDataAdapter.ChatDataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDataAdapter.ChatDataViewHolder holder, int position) {
        holder.userName.setText(arrayList.get(position).getUserName());
        holder.message.setText(arrayList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ChatDataViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView message;
        public ChatDataViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName=itemView.findViewById(R.id.chatList_textView_user);
            this.message=itemView.findViewById(R.id.chatList_textView_message);
        }
    }
}
