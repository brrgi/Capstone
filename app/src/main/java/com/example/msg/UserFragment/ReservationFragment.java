package com.example.msg.UserFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msg.Api.AuthenticationApi;
import com.example.msg.Api.ReserveApi;
import com.example.msg.DatabaseModel.ReserveModel;
import com.example.msg.MainActivity;
import com.example.msg.R;
import com.example.msg.RecyclerView.ReserveAdapter;
import com.example.msg.Reservation.ReservationActivity;

import java.util.ArrayList;


public class ReservationFragment extends Fragment {
    private View view;
    private Button btn_reservation;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView.Adapter reserveAdapter;
    private ArrayList<ReserveModel> reserveModelArrayList = new ArrayList<ReserveModel>();

    private void initializeLayout(final Context context) {
        recyclerView = view.findViewById(R.id.reserve_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        reserveAdapter = new ReserveAdapter(reserveModelArrayList,context);

    }

    private void ReserveHistory() {
        ReserveApi.getReservationListById(AuthenticationApi.getCurrentUid(), new ReserveApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<ReserveModel> reserveModels) {
                recyclerView.setAdapter(reserveAdapter);
                reserveModelArrayList.clear();
                reserveModelArrayList.addAll(reserveModels);
                reserveAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(int errorCode, Exception e) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_reservation,container,false);
        btn_reservation = (Button) view.findViewById(R.id.fragment_reservation_button);
        ReserveHistory();
        initializeLayout(container.getContext());
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ReservationActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
