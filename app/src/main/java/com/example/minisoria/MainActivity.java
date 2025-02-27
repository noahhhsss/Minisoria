package com.example.minisoria;

import static android.app.PendingIntent.getActivity;

import android.content.ClipData;
import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    private  TextView signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    usernameEditText = findViewById(R.id.editTextText);
    passwordEditText = findViewById(R.id.editTextTextPassword);
    loginButton = findViewById(R.id.button);
    signUpBtn = findViewById(R.id.signUpBtn);

    loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(MainActivity.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Login succesful",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);

                finish();
            }
        }
    });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}