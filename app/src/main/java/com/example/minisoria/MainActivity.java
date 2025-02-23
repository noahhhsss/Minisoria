package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    usernameEditText = findViewById(R.id.editTextText);
    passwordEditText = findViewById(R.id.editTextTextPassword);
    loginButton = findViewById(R.id.button);
    rememberMeCheckbox = findViewById(R.id.checkBox);

    loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(MainActivity.this, "please fill in both fields", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Login succesful",Toast.LENGTH_SHORT).show();
                if(rememberMeCheckbox.isChecked());{
                    Toast.makeText(MainActivity.this,"You will be remembered.",Toast.LENGTH_SHORT).show();

                }
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

                finish();
            }
        }
    });

    }
}