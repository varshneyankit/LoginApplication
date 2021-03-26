package com.example.loginapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapplication.R;
import com.example.loginapplication.ui.login.Data.SharedPreferencesConfig;

public class DashboardActivity extends AppCompatActivity {
    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());
    }

    public void logout(View view) {
        Toast.makeText(this, "You are successfully logged out", Toast.LENGTH_LONG).show();
        preferencesConfig.writeLogInStatus(false);
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}