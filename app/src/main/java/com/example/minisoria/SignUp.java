package com.example.minisoria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText Name, Email, Password, Confirmpassword;
    private Button signbutton;
    private TextView Login;
    private CheckBox check;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = findViewById(R.id.border1);
        Email = findViewById(R.id.border2);
        Password = findViewById(R.id.border3);
        Confirmpassword = findViewById(R.id.border4);
        signbutton = findViewById(R.id.button2);
        Login = findViewById(R.id.login);
        check = findViewById(R.id.checkb);

        Login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Sign In button logic (actually creates account and logs in)
        signbutton.setOnClickListener(v -> {
            String Fullname = Name.getText().toString().trim();
            String Emails = Email.getText().toString().trim();
            String Passwords = Password.getText().toString().trim();
            String Confirmpass = Confirmpassword.getText().toString().trim();

            if (Fullname.isEmpty() || Emails.isEmpty() || Passwords.isEmpty() || Confirmpass.isEmpty()) {
                Toast.makeText(SignUp.this, "Please input all credentials", Toast.LENGTH_SHORT).show();
            } else if (!Passwords.equals(Confirmpass)) {
                Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (!check.isChecked()) {
                Toast.makeText(SignUp.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUp.this, "Login successful!", Toast.LENGTH_SHORT).show();
                // Redirect to dashboard or home after login
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
