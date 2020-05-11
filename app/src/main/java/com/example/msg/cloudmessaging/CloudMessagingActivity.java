package com.example.msg.cloudmessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.msg.MainActivity;
import com.example.msg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CloudMessagingActivity extends AppCompatActivity implements View.OnClickListener{


    final String url = "http://192.168.56.1:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        Button tokenbtn = (Button)findViewById(R.id.tokenbtn);
        tokenbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.tokenbtn:
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()) {
                            Log.d("ParkKyudong","getInstanceId failed",task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        String msg = "InstanceId TOKEN: "+token;
                        Log.d("ParkKyudong",msg);
                       // requestToken(token,url);
                        Toast.makeText(CloudMessagingActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
               /* FirebaseMessaging.getInstance().subscribeToTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "hi!!";
                                if (!task.isSuccessful()) {
                                   // msg = getString(R.string.msg_subscribe_failed);
                                }

                                //Toast.makeText(CloudMessagingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });*/
                break;
            default:
                break;
        }
    }

    void requestToken(String token, String url)
    {
        String url1 = url;
        JSONObject testjson = new JSONObject();

        try {
            testjson.put("id",token);
            String jsonString = testjson.toString();

            Log.d("ParkKyudong","testjson?" + jsonString);

            Log.d("ParkKyudong","token?" + token);
            Log.d("ParkKyudong","url?" + url);

            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, testjson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("kyudong", "토큰 전송성공");
                        JSONObject jsonObject = new JSONObject(response.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }

            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();;
        }
    }


}
