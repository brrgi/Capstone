package com.example.msg.UserFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msg.MainActivity;
import com.example.msg.R;
import com.example.msg.Reservation.ReservationActivity;


public class ReservationFragment extends Fragment {
    private View view;
    private Button btn_reservation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_reservation,container,false);
        btn_reservation = (Button) view.findViewById(R.id.fragment_reservation_button);

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
