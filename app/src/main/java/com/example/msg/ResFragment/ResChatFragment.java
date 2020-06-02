package com.example.msg.ResFragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.R;
import com.example.msg.RecyclerView.ChatData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ResChatFragment extends Fragment {
    private View view;
    private static final String TAG = "ChatFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_reschat,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = view.getContext();
        recyclerView=view.findViewById(R.id.chat_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        //layoutManager = new LinearLayoutManager(getActivity()); //fragment일 때 LinearLayoutManager 인자
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();
        //Query query=db.collection("products");
        db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        ChatData chatdata=document.toObject(ChatData.class);
                        arrayList.add(chatdata);
                    }
                    adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                }
                else{
                    Log.d(TAG,"Error getting documents",task.getException());
                }

            }

        });


    }
}


