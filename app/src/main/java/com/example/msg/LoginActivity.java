package com.example.msg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.example.msg.cloudmessaging.CloudMessagingActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText id;
    private EditText password;

    private Button login;
    private Button signup;
    private FirebaseRemoteConfig FirebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseRemoteConfig =FirebaseRemoteConfig.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        String splash_background = FirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        id=(EditText)findViewById(R.id.loginActivity_edittext_id);
        password=(EditText)findViewById(R.id.loginActivity_edittext_password);


        login=(Button)findViewById(R.id.loginActivity_button_login);
        signup =(Button)findViewById(R.id.loginActivity_button_signup);
        login.setBackgroundColor(Color.parseColor(splash_background));
        signup.setBackgroundColor(Color.parseColor(splash_background));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SelectSignupActivity.class));
            }
        });

        //로그인 인터페이스 리스너
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    //로그인

                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    //먼저 user 검사
                    DocumentReference docRef=db.collection("User").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document=task.getResult();
                                if(document.exists()){
                                    Log.d(TAG,"user "+document.getData());

                                    if((Boolean)document.getData().get("sanction")){
                                        Log.d(TAG,"The user is suspended");
                                        Toast.makeText(getApplicationContext(), "신고 누적으로 제재된 사용자입니다.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                    else{
                                        Log.d(TAG,"user login success");
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else {
                                    //식당 사용자일때
                                    Log.d(TAG, "Go restuser");
                                }
                            }
                            else{
                                Log.d(TAG,"User error");
                            }
                        }
                    });

                    //restaurant_user 검사
                    docRef=db.collection("Restaurant").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document=task.getResult();
                                if(document.exists()){
                                    Log.d(TAG,"Restuser "+document.getData());
                                    if((Boolean) document.getData().get("approved")){
                                        Log.d(TAG,"approved Restuser");
                                        Intent intent=new Intent(LoginActivity.this,ResMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Log.d(TAG,"not yet approved Restuser");
                                        Toast.makeText(getApplicationContext(), "승인 완료되지 않은 사용자입니다. 조금만 기다려주시면 승인 도와드리겠습니다.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }

                                }
                                else
                                    Log.d(TAG,"No such user");
                            }
                            else{
                                Log.d(TAG,"RestaurantUser error");
                            }
                        }
                    });

                }else{
                    //로그아웃
                }
            }
        };

        //푸시 테스트를 위한 토큰 테스트 버튼
        Button firebasecloudmessagingbtn = (Button)findViewById(R.id.firebasecloudmessagingbtn);
        firebasecloudmessagingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,CloudMessagingActivity.class));
            }
        });


    }



    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {    //로그인이 정상적으로 완료되었는지 판단 (로그인 후 다른 화면으로 넘겨주는 역학이 아님)
                if(!task.isSuccessful()){
                    //로그인 실패
                    Toast.makeText(LoginActivity.this,"다시 로그인 해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


}
