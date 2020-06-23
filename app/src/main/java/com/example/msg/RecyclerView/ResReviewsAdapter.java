package com.example.msg.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.SaleApi;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SaleModel;
import com.example.msg.Profile.ViewUserProfileActivity;
import com.example.msg.R;
import com.example.msg.Sale.SaleActivity;

import java.util.ArrayList;

public class ResReviewsAdapter extends RecyclerView.Adapter<ResReviewsAdapter.ResReviewsHolder>{
    private ArrayList<SaleModel> arrayList;
    private Context context;


    public ResReviewsAdapter(ArrayList<SaleModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resreview_item,parent,false);
        ResReviewsHolder holder=new ResReviewsHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ResReviewsAdapter.ResReviewsHolder holder, final int position) {

            holder.name.setText(arrayList.get(position).user_name);
            holder.grade.setRating(arrayList.get(position).ratingScore);
            holder.categorySmall.setText(arrayList.get(position).categorySmall);
            holder.review.setText(arrayList.get(position).review);

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ResReviewsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView categorySmall;
        TextView review;
        RatingBar grade;


        public ResReviewsHolder(@NonNull View itemView) {
            super(itemView);
            this.name=itemView.findViewById(R.id.resreview_item_textView_name);
            this.categorySmall=itemView.findViewById(R.id.resreview_item_textView_categorysmall);
            this.review=itemView.findViewById(R.id.resreview_item_textView_review);
            this.grade=itemView.findViewById(R.id.resreview_item_ratingBar_grade);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        SaleModel item = arrayList.get(pos);
                        //아이템을 얻는 부분.
                        Intent intent = new Intent(v.getContext(), ViewUserProfileActivity.class);
                        intent.putExtra("user_id", item.user_id);
                        v.getContext().startActivity(intent);


                    }
                }
            });
            //여기서 리사이클러뷰의 아이템이 클릭되는 것을 처리할 수 있음.
        }
    }
}
