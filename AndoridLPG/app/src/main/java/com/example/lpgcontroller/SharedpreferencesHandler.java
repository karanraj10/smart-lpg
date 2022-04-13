package com.example.lpgcontroller;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferencesHandler {

    public static SharedPreferences sharedPreferences ;
    public static SharedPreferences.Editor editor;

    public SharedpreferencesHandler(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginData",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn",false);
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("isLoggedIn",loggedIn);
        editor.commit();
    }

    public String getUidFirebase() {
        return sharedPreferences.getString("uidFirebase","");
    }

    public void setUidFirebase(String uidFirebase) {
        editor.putString("uidFirebase",uidFirebase);
        editor.commit();
    }
}
