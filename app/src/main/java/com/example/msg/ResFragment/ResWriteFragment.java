package com.example.msg.ResFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.msg.R;
import com.example.msg.Upload.ProductRestUploadActivity;

import static android.app.Activity.RESULT_OK;


public class ResWriteFragment extends Fragment {
    private static final String TAG = "WriteFragment";
    private static final int PICK_FROM_ALBUM = 10;
    private View view;
    private Button enrollment;
    private ImageView Image;
    private Uri imageUri;
    private EditText title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reswrite, container, false);


        enrollment = (Button) view.findViewById(R.id.writeFragment_button_enrollment);

        enrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductRestUploadActivity.class);
                startActivity(intent);
            }

        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_FROM_ALBUM && resultCode==RESULT_OK){
            imageUri=data.getData();    //이미지 원본 경로
            Image.setImageURI(imageUri);
        }
    }

}


