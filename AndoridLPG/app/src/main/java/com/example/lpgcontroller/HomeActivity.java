package com.example.lpgcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    ChipNavigationBar navigationBar;
    Fragment fragment;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String topic = SplashActivity.sharedpreferencesHandler.getUidFirebase();
        topic= topic.replace(":","");
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        fragmentManager = getSupportFragmentManager();

        navigationBar = findViewById(R.id.bottomNavigationBar);

        navigationBar.setItemSelected(R.id.dashboardMenuItem,true);
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,new DashboardFragment(),null).commit();

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {

                switch (i)
                {
                    case R.id.dashboardMenuItem:
                        fragment = new DashboardFragment();
                        break;

                    case R.id.dailyUsageMenuItem:
                        fragment = new DailyUsageFragment();
                        break;

                    case R.id.usageHistoryMenuItem:
                        fragment = new LpgHistoryFragment();
                        break;

                    case R.id.accountMenuItem:
                        fragment = new AccountFragment();
                        break;

                }
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment,null).commit();
            }
        });
    }
}