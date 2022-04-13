package com.example.lpgcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText macEditText, passwordEditText;
    Button loginButtonView;
    ProgressAlertDialog progressAlertDialog;

    CardView bottomCardView;
    Intent intent;

    String macAddress, password;
    boolean ISACCOUNTSETUP;

    DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Find View's ID
        bottomCardView = findViewById(R.id.cardViewBottomLogin);
        macEditText = findViewById(R.id.macAddressLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButtonView = findViewById(R.id.loginButton);

        // initialize ProgressAlertDialog
        progressAlertDialog = new ProgressAlertDialog(MainActivity.this);

        // Load Animation From XML File (slide_up_activity)
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up_activity);
        // Apply Animation to CardView
        bottomCardView.startAnimation(animation);

        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                macAddress = macEditText.getText().toString().toUpperCase(Locale.ROOT);
                password = passwordEditText.getText().toString();
                loginUser(macAddress,password);
            }
        });

        macEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==2||editable.length()==5||editable.length()==8||editable.length()==11||editable.length()==14){
                    editable.append(":");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });
    }

    private void loginUser(String macAddress, String password) {

        if (macAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, getApplicationContext().getString(R.string.empty_inputs_toast), Toast.LENGTH_SHORT).show();
        }
        else{
            progressAlertDialog.startProgressAlertDialog();
            isAccountSetup(macAddress,password);
        }
    }

    private void isAccountSetup(String macAddress, String password)
    {
        // Reference to particular User in database by it's MAC
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress);
        // Listener for fetching single item
        userDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String fetchedPassword = String.valueOf(snapshot.child(getApplicationContext().getString(R.string.password)).getValue());
                ISACCOUNTSETUP = fetchedPassword.equals(password);

                // Handler for delay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressAlertDialog.stopProgressAlertDialog();
                        if (ISACCOUNTSETUP)
                        {
                            SplashActivity.sharedpreferencesHandler.setLoggedIn(true);
                            SplashActivity.sharedpreferencesHandler.setUidFirebase(macAddress);

                            progressAlertDialog.stopProgressAlertDialog();
                            updateUI(HomeActivity.class);

                        }
                        else{
                            progressAlertDialog.stopProgressAlertDialog();
                            Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.invalid_login_error),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Long.parseLong(getApplicationContext().getString(R.string.delay_for_button)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressAlertDialog.stopProgressAlertDialog();
                Log.d("FIREBASE",""+error);
                Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(Class cls)
    {
        intent = new Intent(MainActivity.this,cls);
        startActivity(intent);
        finish();
    }
}