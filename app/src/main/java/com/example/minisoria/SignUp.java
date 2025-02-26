package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText Name, Email ,Password;
    private Button signbutton;

    private TextView Signin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name=findViewById(R.id.editTextText2);
        Email=findViewById(R.id.editTextText3);
        Password=findViewById(R.id.editTextTextPassword2);

        signbutton=findViewById(R.id.button2);
        Signin=findViewById(R.id.signup);

        signbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fullname = Name.getText().toString();
                String Emails =Email.getText().toString();
                String Passwords = Password.getText().toString();
                if (Fullname.isEmpty() || Emails.isEmpty() || Passwords.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please input credentials",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(SignUp.this, "Please login", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}

