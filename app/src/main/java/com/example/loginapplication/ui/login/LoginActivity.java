package com.example.loginapplication.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginapplication.R;
import com.example.loginapplication.ui.login.Data.SharedPreferencesConfig;

public class LoginActivity extends AppCompatActivity {

    private final static int SMS_PERMISSION_REQUEST_CODE = 101;
    private SharedPreferencesConfig preferencesConfig;
    private ProgressBar loadingProgressBar;
    private EditText mobileEditText, passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        mobileEditText = findViewById(R.id.mobileNumber);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        if (!checkPermissions())
            requestPermissions();
        if (preferencesConfig.readLogInStatus()) {
            if (getSmsConfirmation())
                navigateToDashboard();
            else {
                preferencesConfig.writeLogInStatus(false);
                Toast.makeText(LoginActivity.this, "SMS not found, please login again", Toast.LENGTH_LONG).show();
            }
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginButton.setEnabled(!mobileEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty());
            }
        };
        mobileEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
    }

    private boolean getSmsConfirmation() {
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");
        // List required columns
        String[] columns = new String[]{"body"};
        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor cursor = getContentResolver().query(inboxURI, columns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }

                if (msgData.contains("confirmare")) {
                    cursor.close();
                    //Log.e("TAG", msgData);
                    return true;
                }
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            Toast.makeText(LoginActivity.this, "Inbox is empty", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (this != null) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private void navigateToDashboard() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    public void login(View view) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        if (mobileEditText.getText().toString().equals("123456789") && passwordEditText.getText().toString().equals("admin")) {
            preferencesConfig.writeLogInStatus(true);
            navigateToDashboard();
        } else {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Incorrect mobile number or password detected", Toast.LENGTH_LONG).show();
        }
    }
}