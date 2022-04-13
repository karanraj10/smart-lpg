package com.example.lpgcontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class UsagesHistoryFragment extends Fragment {

    View view;
    Context context;
    TextView lpgId;
    RecyclerView usageListView;
    ArrayList<MyData> usageDataModelArrayList;
    DailyListAdapter customAdapter;
    String lpgID;
    Button reportButton;

    ProgressAlertDialog progressAlertDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        lpgID = getArguments().getString("lpgID");

        view =  inflater.inflate(R.layout.fragment_usages_history, container, false);
        lpgId = view.findViewById(R.id.usagesHistoryLpgHeader);
        usageListView = view.findViewById(R.id.usagesHistoryListView);
        reportButton = view.findViewById(R.id.generateReportButton);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,LpgReportActivity.class);
                intent.putExtra("lpgID",lpgID);
                context.startActivity(intent);
            }
        });

        progressAlertDialog = new ProgressAlertDialog(getActivity());
        progressAlertDialog.startProgressAlertDialog();

        lpgId.setText("Of Lpg: "+lpgID);

        // Initialize List
        usageDataModelArrayList = new ArrayList<>();

        loadUsagesData(lpgID);

        // Initialize CustomAdapter
        customAdapter = new DailyListAdapter(getContext(),usageDataModelArrayList);

        // Setup RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        usageListView.setLayoutManager(linearLayoutManager);
        usageListView.setAdapter(customAdapter);

        return view;
    }

    private void loadUsagesData(String id){

        String macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();
        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).child(id).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usageDataModelArrayList.clear();

                for (DataSnapshot s:snapshot.child("usage").getChildren()) {

                    MyData myData = new MyData();
                    myData.setTimeSlots((HashMap<String, Float>) s.getValue());
                    myData.setDate(s.getKey());
                    usageDataModelArrayList.add(myData);
                }
                Collections.sort(usageDataModelArrayList,new MapComparator());
                Collections.reverse(usageDataModelArrayList);
                progressAlertDialog.stopProgressAlertDialog();
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressAlertDialog.stopProgressAlertDialog();
                Toast.makeText(context,context.getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
            }
        });
    }
}