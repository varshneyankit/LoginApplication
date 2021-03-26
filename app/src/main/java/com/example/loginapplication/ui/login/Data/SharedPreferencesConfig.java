package com.example.loginapplication.ui.login.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesConfig {
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.example.loginapplication.ui.login.Data_preferences", Context.MODE_PRIVATE);
    }

    //log in status
    public void writeLogInStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logInStatus", status);
        editor.commit();
    }

    public boolean readLogInStatus() {
        return sharedPreferences.getBoolean("logInStatus", false);
    }

}
