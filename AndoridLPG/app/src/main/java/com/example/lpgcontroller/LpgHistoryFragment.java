package com.example.lpgcontroller;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LpgHistoryFragment extends Fragment {

    View view;
    Context context;
    RecyclerView recyclerView;
    ArrayList<LPGData> arrayList;
    LpgListAdapter customAdapter;
    SearchView searchView;

    ProgressAlertDialog progressAlertDialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_lpg_history, container, false);

        recyclerView = view.findViewById(R.id.lpgHistoryList);
        searchView = view.findViewById(R.id.lpgHistorySearch);

        progressAlertDialog = new ProgressAlertDialog(getActivity());
        progressAlertDialog.startProgressAlertDialog();

        // Initialize List
        arrayList = new ArrayList<>();

        loadLpgList();

        // Initialize CustomAdapter
        customAdapter = new LpgListAdapter(getContext(),arrayList);

        // Setup RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(customAdapter);

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

        return view;
    }

    private void loadLpgList(){

        String macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot s:snapshot.getChildren()) {
                    LPGData lpgData = s.getValue(LPGData.class);
                    lpgData.setLpgID(s.getKey());
                    arrayList.add(lpgData);
                }

                Collections.reverse(arrayList);
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