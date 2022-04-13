package com.example.lpgcontroller;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import app.futured.donut.DonutSection;

public class DailyUsageFragment extends Fragment {


    View view;
    RecyclerView dailyListView;
    ArrayList<MyData> usageDataModelArrayList;
    DailyListAdapter customAdapter;
    Context context;
    TextView currentLpgView;
    String currentLpg;
    SearchView searchView;
    HashMap<String,HashMap<String,Float>> myHashmap;

    ProgressAlertDialog progressAlertDialog;

    // To Get Context
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_daily_usage, container, false);
        dailyListView=view.findViewById(R.id.dailyUsageList);
        searchView = view.findViewById(R.id.dailyUsagesSearch);

        progressAlertDialog = new ProgressAlertDialog(getActivity());
        progressAlertDialog.startProgressAlertDialog();

        loadDailyList();

        currentLpgView = view.findViewById(R.id.dailyUsageLpgHeader);

        // Initialize List
        usageDataModelArrayList = new ArrayList<>();

        // Initialize CustomAdapter
        customAdapter = new DailyListAdapter(getContext(),usageDataModelArrayList);

        // Setup RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dailyListView.setLayoutManager(linearLayoutManager);
        dailyListView.setAdapter(customAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return  view;
    }

    private void loadDailyList() {

        String macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usageDataModelArrayList.clear();

                for (DataSnapshot s:snapshot.getChildren()) {

                    currentLpg = s.getKey();
                    currentLpgView.setText(new StringBuilder().append("Of Currently Running Lpg: ").append(currentLpg).toString());

                    for(DataSnapshot ds:s.child("usage").getChildren()){
                        MyData myData = new MyData();
                        myData.setTimeSlots((HashMap<String, Float>) ds.getValue());
                        myData.setDate(ds.getKey());
                        usageDataModelArrayList.add(myData);
                    }
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