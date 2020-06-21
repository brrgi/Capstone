package com.example.msg.ChatRoom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ChatRoomApi;
import com.example.msg.DatabaseModel.ChatRoomModel;
import com.example.msg.R;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private ArrayList<ChatRoomModel> chatRoomModels;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView lastTime;
        TextView lastChat;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.chat_room_list_item_textView_name);
            lastTime = itemView.findViewById(R.id.chat_room_list_item_textView_time);
            lastChat = itemView.findViewById(R.id.chat_room_list_item_textView_last_chat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        ChatRoomModel chatRoomModel = chatRoomModels.get(pos);
                        Intent intent = new Intent(v.getContext(), ChatRoomActivity.class);
                        intent.putExtra("object", chatRoomModel);
                        v.getContext().startActivity(intent);

                    }

                }
            });
        }
    }



    public ChatRoomAdapter(ArrayList<ChatRoomModel> list) {
        chatRoomModels = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_room_list_item, parent, false);


        ChatRoomAdapter.ViewHolder viewHolder = new ChatRoomAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = ChatRoomApi.getOpponentNameByModel(chatRoomModels.get(position), AuthenticationApi.getCurrentUid());
        String date = chatRoomModels.get(position).lastDate;
        String chat = chatRoomModels.get(position).lastChat;

        holder.lastChat.setText(chat);
        holder.lastTime.setText(date);
        holder.name.setText(name);

    }
    //여기서 내용물을 설정함.

    @Override
    public int getItemCount() {
        return chatRoomModels.size();
    }

}
