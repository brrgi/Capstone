package com.example.msg.ChatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Chat> chatData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.testText);
            //여기서 레이아웃 초기화.
        }
    }

    ChatAdapter(ArrayList<Chat> list) {
        chatData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.chat_item, parent, false);
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = chatData.get(position).takeContent();
        holder.textView.setText(text);
    }
    //여기서 내용물을 설정함.

    @Override
    public int getItemCount() {
        return chatData.size();
    }
}
