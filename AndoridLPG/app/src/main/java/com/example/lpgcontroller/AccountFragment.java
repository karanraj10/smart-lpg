package com.example.lpgcontroller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AccountFragment extends Fragment {

    Context context;
    View view;
    TextView macAddressView;
    Intent intent;

    ListView accountSettingsListView;
    ArrayList<String> settingsList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_account, container, false);
        macAddressView = view.findViewById(R.id.accountMacAddress);

        String macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();
        macAddressView.setText(macAddress);

        accountSettingsListView = view.findViewById(R.id.accountSettingsListView);
        settingsList = new ArrayList<>();
        settingsList.add(context.getString(R.string.updateDetailsSetting));
        settingsList.add(context.getString(R.string.changePasswordSetting));
        settingsList.add(context.getString(R.string.requestNewLpg));
        settingsList.add(context.getString(R.string.LogoutSetting));
        accountSettingsListView.setAdapter(new CustomAdapter(context));

        accountSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i)
                {
                    case 0:
                        intent = new Intent(getActivity(),UpdateDetailsActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(getActivity(),EditPasswordActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(getActivity(),RequestLpgActivity.class);
                        startActivity(intent);
                        break;

                    case 3:

                        // Logout...
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);

                        builder.setMessage(context.getString(R.string.LogoutAlertMessage));
                        builder.setPositiveButton(context.getString(R.string.AlertYesButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logoutUser();
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
                        break;
                }
            }
        });

        return view;
    }

    public class CustomAdapter extends BaseAdapter
    {
        Context context;
        TextView accountSettingTextView;

        CustomAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount() {
            return settingsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.account_setting_list_item, null, false);

            accountSettingTextView = view.findViewById(R.id.accountSettingTextView);
            accountSettingTextView.setText(settingsList.get(i));

            return view;
        }
    }

    private void logoutUser()
    {
        final Activity activity = getActivity();

        SplashActivity.sharedpreferencesHandler.setLoggedIn(false);
        SplashActivity.sharedpreferencesHandler.setUidFirebase(null);
        intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }
}