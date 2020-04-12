package com.example.msg.fragment;


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
import com.example.msg.recyclerView.Products;
import com.example.msg.recyclerView.ProductsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private View view;
    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Products> arrayList;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = view.getContext();
        recyclerView=view.findViewById(R.id.home_recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        //layoutManager = new LinearLayoutManager(getActivity()); //fragment일 때 LinearLayoutManager 인자
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();
        //Query query=db.collection("products");
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Products products=document.toObject(Products.class);
                        arrayList.add(products);
                        Log.d(TAG,"add products in arrayList "+products.getTitle());
                    }
                    adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                }
                else{
                    Log.d(TAG,"Error getting documents",task.getException());
                }

            }

        });


        adapter=new ProductsAdapter(arrayList,context);
        recyclerView.setAdapter(adapter);
    }

}
