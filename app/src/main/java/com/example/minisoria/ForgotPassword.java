package com.example.minisoria;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.db.DatabaseHelper;

public class ForgotPassword extends AppCompatActivity {

    EditText usernameEditText, emailEditText, newPasswordEditText;
    Button resetPasswordButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        db = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.validateUserForReset(username, email)) {
                    boolean updated = db.resetPassword(username, newPassword);
                    if (updated) {
                        Toast.makeText(ForgotPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        finish(); // go back to login screen
                    } else {
                        Toast.makeText(ForgotPassword.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, "Invalid username or email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
