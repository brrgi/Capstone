package com.example.msg.UserFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msg.R;
import com.example.msg.Sale.SaleActivity;
import com.example.msg.Upload.ProductUploadActivity;


public class WriteFragment extends Fragment {
    private static final String TAG = "WriteFragment";
    private static final int PICK_FROM_ALBUM = 10;
    private View view;
    private Button enrollment;
    private Button upload;
    private ImageView Image;
    private Uri imageUri;
    private EditText title;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_write, container, false);

        upload = (Button)view.findViewById(R.id.writeFragment_button_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductUploadActivity.class);
                startActivity(intent);
            }
        });

        enrollment = (Button) view.findViewById(R.id.writeFragment_button_enrollment);
        enrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SaleActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "업로드로 갑니다.", Toast.LENGTH_LONG).show();
            }
        });

//        upload = (Button) view.findViewById(R.id.writeFragment_button_enrollment);
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UploadActivity.class);
//                startActivity(intent);
//                Toast.makeText(getActivity(), "업로드로 갑니다.", Toast.LENGTH_LONG).show();
//            }
//        });

        return view;
    }


}

