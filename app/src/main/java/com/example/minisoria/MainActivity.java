package com.example.minisoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    private TextView signUpBtn,forgot;
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
        forgot = findViewById(R.id.forgotpass);
        DB = new DatabaseHelper(this);

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
                    // Save "remember me" data
                    SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                    if (cb.isChecked()) {
                        editor.putBoolean("remember", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    // Save logged-in username for other activities like Draweraccountinfo
                    SharedPreferences userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor userEditor = userPrefs.edit();
                    userEditor.putString("username", username);
                    userEditor.apply();

                    if (username.equals("admin") && password.equals("Admin123456") || (username.equals("noaqim") && password.equals("noa"))) {
                        Toast.makeText(MainActivity.this, "Hi, Admin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Admindashboard.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Dashboardwdrawer.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(i);
            }
        });
    }
}
