package com.example.lpgcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;

public class UpdateDetailsActivity extends AppCompatActivity {

    EditText customerNumberEditText, mobileEditText;
    String customerNumber, mobileNumber, macAddress;
    Button saveDetailsButton;
    ProgressAlertDialog progressAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        progressAlertDialog = new ProgressAlertDialog(this);
        progressAlertDialog.startProgressAlertDialog();

        macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        saveDetailsButton = findViewById(R.id.saveDetailsButton);
        customerNumberEditText = findViewById(R.id.customerNumberEditText);
        mobileEditText = findViewById(R.id.mobileNumberEditText);

        FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("customerNumber").exists()){
                    String fetchedCustomerNumber = String.valueOf(snapshot.child("customerNumber").getValue());
                    customerNumberEditText.setText(fetchedCustomerNumber);
                }
                if(snapshot.child("mobileNumber").exists()){
                    String fetchedMobile = String.valueOf(snapshot.child("mobileNumber").getValue());
                    mobileEditText.setText(fetchedMobile);
                }
                progressAlertDialog.stopProgressAlertDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
                progressAlertDialog.stopProgressAlertDialog();
            }

        });

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobileNumber = mobileEditText.getText().toString();
                customerNumber = customerNumberEditText.getText().toString();

                if (mobileNumber.isEmpty() || customerNumber.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Provide Inputs!",Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).child("customerNumber").setValue(customerNumber);
                    FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).child("mobileNumber").setValue(mobileNumber);

                    Toast.makeText(getApplicationContext(),"Details Saved Successfully!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}