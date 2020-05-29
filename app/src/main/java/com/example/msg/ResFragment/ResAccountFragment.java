package com.example.msg.ResFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.msg.R;
import com.example.msg.Sale.ResSalesHistoryActivity;


public class ResAccountFragment extends Fragment {
    private View view;
    private LinearLayout ressaleshistory;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_resaccount,container,false);
        ressaleshistory=view.findViewById(R.id.resaccount_linearLayout_saleshistory);

        ressaleshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResSalesHistoryActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
