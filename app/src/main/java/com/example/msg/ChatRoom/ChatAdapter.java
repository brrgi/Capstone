package com.example.msg.ChatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Chat> chatData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myContent;
        TextView myDate;
        TextView opponentContent;
        TextView opponentDate;
        String myUid;

        ViewHolder(View itemView) {
            super(itemView);

            myContent = itemView.findViewById(R.id.chat_item_mine_textView_content);
            myDate = itemView.findViewById(R.id.chat_item_mine_textView_time);

            opponentContent = itemView.findViewById(R.id.chat_item_opponent_textView_content);
            opponentDate = itemView.findViewById(R.id.chat_item_opponent_textView_time);

            myUid = AuthenticationApi.getCurrentUid();
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
        View view;

        if(viewType == 1) {
            view = inflater.inflate(R.layout.chat_item_mine, parent, false);
        }
        else {
            view = inflater.inflate(R.layout.chat_item_opponent, parent, false);
        }
        //여기서 뷰 아이템을 지정함.

        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = chatData.get(position).takeContent();
        String date = chatData.get(position).getDate();
        //
        boolean isMine = chatData.get(position).isMine(holder.myUid);

        if(isMine) {
            holder.myContent.setText(text);
            holder.myDate.setText(date);
        } else {
            holder.opponentContent.setText(text);
            holder.opponentDate.setText(date);
        }


    }
    //여기서 내용물을 설정함.

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    @Override
    public int getItemViewType(int position) {
        String myUid  = AuthenticationApi.getCurrentUid();
        boolean isMine = chatData.get(position).isMine(myUid);

        if(isMine) return 1;
        else return 0;
    }
}
