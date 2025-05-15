package com.example.minisoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpBtn;
    private CheckBox cb;

    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.passwordsignin);
        loginButton = findViewById(R.id.button);
        signUpBtn = findViewById(R.id.signUpBtn);
        cb = findViewById(R.id.checkBox);
        DB = new DatabaseHelper(this);

        // Load saved credentials if "Remember Me" was checked
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isRemembered = preferences.getBoolean("remember", false);

        if (isRemembered) {
            String savedUsername = preferences.getString("username", "");
            String savedPassword = preferences.getString("password", "");
            usernameEditText.setText(savedUsername);
            passwordEditText.setText(savedPassword);
            cb.setChecked(true);
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = DB.checkUsernamePassword(username, password);
                if (isValid) {
                    // Save or clear credentials based on checkbox
                    SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                    if (cb.isChecked()) {
                        editor.putBoolean("remember", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Dashboardwdrawer.class));
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });
    }
}
