package com.example.lpgcontroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestLpgActivity extends AppCompatActivity {

    Button requestLpgButton;
    TextView customerNumberTextView, mobileNumberTextView;
    String fetchedCustomerNumber="", fetchedMobileNumber="",macAddress,url,ip;
    ProgressAlertDialog progressAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lpg);

        macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        progressAlertDialog = new ProgressAlertDialog(this);
        progressAlertDialog.startProgressAlertDialog();

        customerNumberTextView = findViewById(R.id.customerNumberTextView);
        mobileNumberTextView = findViewById(R.id.mobileNumberTextView);
        requestLpgButton = findViewById(R.id.requestLpgButton);

        FirebaseDatabase.getInstance().getReference().child("params").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("localhost").exists()){
                    ip = String.valueOf(snapshot.child("localhost").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
            }

        });

        FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("customerNumber").exists()){
                    fetchedCustomerNumber = String.valueOf(snapshot.child("customerNumber").getValue());
                    customerNumberTextView.setText(fetchedCustomerNumber);
                }
                if(snapshot.child("mobileNumber").exists()){
                    fetchedMobileNumber = String.valueOf(snapshot.child("mobileNumber").getValue());
                    mobileNumberTextView.setText(fetchedMobileNumber);
                }
                progressAlertDialog.stopProgressAlertDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
                progressAlertDialog.stopProgressAlertDialog();
            }

        });

        requestLpgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fetchedCustomerNumber.isEmpty()||fetchedMobileNumber.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Details before making request!",Toast.LENGTH_SHORT).show();
                }else{
                    if(ip!=null && (!ip.isEmpty())){
                        sendEmail();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Can't connect to Server!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void sendEmail()
    {
        url = ip+"/sendEmail.php";

        progressAlertDialog.startProgressAlertDialog();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = Boolean.parseBoolean(jsonObject.getString("status"));

                    if (status){
                        Toast.makeText(getApplicationContext(),"Email Sent Successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressAlertDialog.stopProgressAlertDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onError: "+error);
                progressAlertDialog.stopProgressAlertDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> values = new HashMap<>();
                values.put("customerNumber",fetchedCustomerNumber);
                values.put("mobileNumber",fetchedMobileNumber);
                return values;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}