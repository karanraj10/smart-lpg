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

public class EditPasswordActivity extends AppCompatActivity {

    EditText passwordEditText, oldPasswordEditText;
    String password, macAddress, oldPassword;
    Button editPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        passwordEditText = findViewById(R.id.passwordEditPassword);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditPassword);
        editPasswordButton = findViewById(R.id.editPasswordButton);

        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = passwordEditText.getText().toString();
                oldPassword = oldPasswordEditText.getText().toString();

                if (password.isEmpty() || oldPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Password should not be empty!",Toast.LENGTH_SHORT).show();
                }
                else if(oldPassword.equals(password)){
                    Toast.makeText(getApplicationContext(),"New Password and Old password can't be same!",Toast.LENGTH_SHORT).show();
                }
                else{

                    FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("password").exists()){
                                String fetchedPassword = String.valueOf(snapshot.child("password").getValue());
                                if (!fetchedPassword.equals(oldPassword)){
                                    Toast.makeText(getApplicationContext(),"Old Password is incorrect!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getString(R.string.users)).child(macAddress).child("password").setValue(password);
                                    Toast.makeText(getApplicationContext(),"Password updated Successfully!",Toast.LENGTH_SHORT).show();
                                    passwordEditText.setText("");
                                    oldPasswordEditText.setText("");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
    }
}