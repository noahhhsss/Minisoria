package com.example.minisoria;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.db.DatabaseHelper;

public class SignUp extends AppCompatActivity {

    DatabaseHelper DB;
    private EditText Name, Email, Password, Confirmpassword;
    private Button signbutton,acceptButton, dialogAcceptButton;
    private TextView Login ,termsandcondi;
    private CheckBox check;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = findViewById(R.id.border1);
        Email = findViewById(R.id.border2);
        Password = findViewById(R.id.password_input);
        Confirmpassword = findViewById(R.id.confirmpass1);
        signbutton = findViewById(R.id.button2);
        Login = findViewById(R.id.login);
        check = findViewById(R.id.checkb);
        termsandcondi = findViewById(R.id.terms);
        acceptButton = findViewById(R.id.acceptButton);
        DB = new DatabaseHelper(this);


        Login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        termsandcondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fullLayout = getLayoutInflater().inflate(R.layout.termsandcondisignup, null);

                View contentOnly = fullLayout.findViewById(R.id.termsContentLayout);

                if (contentOnly.getParent() != null) {
                    ((ViewGroup) contentOnly.getParent()).removeView(contentOnly);
                }

                // Create dialog
                Dialog dialog = new Dialog(SignUp.this);
                dialog.setContentView(contentOnly);
                dialog.setCancelable(true);

                dialogAcceptButton = contentOnly.findViewById(R.id.acceptButton);
                dialogAcceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check.setChecked(true);
                        Toast.makeText(SignUp.this, "You have accepted the terms", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


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
                    boolean insert = DB.insertUser(Fullname, Emails, Passwords);
                    if (insert) {
                        Toast.makeText(SignUp.this, "Account created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Account creation failed (maybe already exists)", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}
