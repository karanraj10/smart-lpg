package com.example.lpgcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import app.futured.donut.DonutSection;

public class ControlValveActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView[] cards = new CardView[2];
    private CardView card_unFocus;
    private int[] cardId = {R.id.onModeCardView, R.id.offModeCardView};
    TextView currentStatusView;

    Button applyValveStatusButton;
    Context context;

    boolean valveStatus;
    boolean currentValveStatus;
    String macAddress, lpgID;
    Intent intent;
    ProgressAlertDialog progressAlertDialog;
    boolean isStatusAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_valve);

        context = this;

        intent = getIntent();
        lpgID = intent.getStringExtra("lpgID");
        macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        progressAlertDialog = new ProgressAlertDialog(this);
        progressAlertDialog.startProgressAlertDialog();

        loadValveStatus();

        applyValveStatusButton = findViewById(R.id.applyValveStatusButton);
        currentStatusView = findViewById(R.id.controlValveStatus);

        // Theme Button Configuration
        for(int i = 0; i < cards.length; i++){
            cards[i] = (CardView) findViewById(cardId[i]);
            cards[i].setOnClickListener(this);
        }

        loadValveStatus();

        // Apply Theme Button Click
        applyValveStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentValveStatus==valveStatus)
                {
                    Toast.makeText(context,context.getString(R.string.AlreadyApplied),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);

                    builder.setTitle(context.getString(R.string.valveStatusAlertHeader));
                    builder.setMessage(context.getString(R.string.valveStatusAlertMessage));
                    builder.setPositiveButton(context.getString(R.string.AlertYesButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeValveStatus();
                            loadValveStatus();
                        }
                    })
                    .setNegativeButton(context.getString(R.string.AlertNoButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(isStatusAvailable){
            switch (view.getId()){
                case R.id.onModeCardView:
                    setFocus(card_unFocus, cards[0]);
                    valveStatus = true;
                    break;

                case R.id.offModeCardView:
                    setFocus(card_unFocus, cards[1]);
                    valveStatus = false;
                    break;
            }
        }
    }

    private void setFocus(CardView card_unFocus, CardView card_focus){
        card_unFocus.setBackgroundResource(R.color.colorGrey);
        card_focus.setBackgroundResource(R.drawable.button_border_background);
        this.card_unFocus = card_focus;
    }

    private void loadValveStatus(){
        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).child(lpgID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(context.getString(R.string.onOffStatus)).exists()){
                    isStatusAvailable = true;
                    valveStatus = Boolean.parseBoolean(String.valueOf(snapshot.child(context.getString(R.string.onOffStatus)).getValue()));
                    currentValveStatus = valveStatus;
                    String onOffStatus;

                    if(valveStatus){
                        setFocus(cards[1],cards[0]);
                        onOffStatus = "ON";
                    }else{
                        setFocus(cards[0],cards[1]);
                        onOffStatus = "OFF";
                    }

                    currentStatusView.setText(new StringBuilder().append("Current Status: ").append(onOffStatus));
                }else{
                    Toast.makeText(context,context.getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
                    applyValveStatusButton.setClickable(false);
                }
                progressAlertDialog.stopProgressAlertDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressAlertDialog.stopProgressAlertDialog();
                Toast.makeText(context,context.getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changeValveStatus(){
        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).child(lpgID).child(context.getString(R.string.onOffStatus)).setValue(valveStatus);
    }

}