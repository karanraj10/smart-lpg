package com.example.lpgcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LpgReportActivity extends AppCompatActivity {

    Intent intent;
    String macAddress, lpgID;
    String html;
    WebView textView;
    Context context;
    FloatingActionButton getPdfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_report);

        context = this;

        textView = findViewById(R.id.reportContent);
        getPdfButton = findViewById(R.id.getPDFButton);

        getPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();
            }
        });

        intent = getIntent();
        lpgID = intent.getStringExtra("lpgID");

        macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        html = "<html><body padding='10%'><h2>User ID: "+macAddress+"</h2><br>";
        html += "<h3> LPG ID: "+lpgID+"</h3>";

        FirebaseDatabase.getInstance().getReference().child(this.getString(R.string.users)).child(macAddress).child(this.getString(R.string.lpg)).child(lpgID).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                html += "<p> LPG Join Date: "+snapshot.child("joinDate").getValue()+"</p>";
                html += "<p> LPG Join Time: "+snapshot.child("joinTime").getValue()+"</p>";
                html += "<p> LPG Capacity: "+snapshot.child("initWeight").getValue()+"</p>";
                html += "<p> LPG Current Weight: "+snapshot.child("currentWeight").getValue()+"</p>";

                html += "<p><b> Daily Usages: <b></p>";

                for (DataSnapshot s:snapshot.child("usage").getChildren()) {

                    html += "<p> Date: "+s.getKey()+" </p>";
                    html += "<table border='1px solid white' style='border-collapse:collapse;'><tr><th>Time</th><th>Gas Used </th></tr>";

                    for (DataSnapshot ss:s.getChildren()){

                        html += "<tr><td>"+ss.getKey()+"</td><td>"+ss.getValue()+"</td></tr>";
                    }
                    html += "</table></body></html>";
                }
                textView.loadData(html,"text/html","UTF-8");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getPDF(){

        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        String jobName = "LPG_"+lpgID+"_REPORT";
        PrintDocumentAdapter printAdapter = textView.createPrintDocumentAdapter(jobName);
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }
}