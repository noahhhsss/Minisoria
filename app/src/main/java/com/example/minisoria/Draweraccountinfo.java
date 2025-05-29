package com.example.minisoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.minisoria.db.DatabaseHelper;

public class Draweraccountinfo extends AppCompatActivity {

    DatabaseHelper db;
    private DrawerLayout drawerlayout;

    private EditText nameInput, emailInput, passInput, confirmPassInput;
    private ImageView editNameIcon, editEmailIcon, editPasswordIcon, editConfirmPasswordIcon;
    private LinearLayout home, order, notification, termsandcondi, contactus, logout;
    private ImageView menu;
    private String oldUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draweraccountinfo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            ((android.view.Window) window).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        db = new DatabaseHelper(this);

        // Get username from Intent or SharedPreferences
        Intent intent = getIntent();
        oldUsername = intent.getStringExtra("username");
        if (oldUsername == null) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            oldUsername = prefs.getString("username", null);
        }

        // Drawer and nav setup
        Button deleteAccountBtn = findViewById(R.id.deletebtn);
        menu = findViewById(R.id.menubtn);
        drawerlayout = findViewById(R.id.drawerlayout);
        home = findViewById(R.id.Home);
        notification = findViewById(R.id.Notif);
        order = findViewById(R.id.Order);
        contactus = findViewById(R.id.Contactus);
        termsandcondi = findViewById(R.id.termsandcondi);
        logout = findViewById(R.id.logoutt);

        // Input fields
        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passInput = findViewById(R.id.password);
        confirmPassInput = findViewById(R.id.confirmpasswords);

        // Edit icons
        editNameIcon = findViewById(R.id.edit_name_icon);
        editEmailIcon = findViewById(R.id.edit_email_icon);
        editPasswordIcon = findViewById(R.id.edit_password_icon);
        editConfirmPasswordIcon = findViewById(R.id.edit_confirmpasswords_icon);

        // Save button
        Button saveBtn = findViewById(R.id.savebtn);

        TextView usersnameTextView = findViewById(R.id.usersname);

        if (oldUsername != null) {
            // Option 1: Just display the username string
            usersnameTextView.setText(oldUsername);

            // Option 2: If you want to display the full name from DB, get it from your db helper
            String[] userInfo = db.getUserInfo(oldUsername);
            if (userInfo != null && userInfo.length >= 1) {
                // Assuming userInfo[0] is full name or display name
                usersnameTextView.setText(userInfo[0]);
            }
        } else {
            // Optional: set default text if username not found
            usersnameTextView.setText("Guest");
        }


        // Load user info from DB
        if (oldUsername != null) {
            String[] userInfo = db.getUserInfo(oldUsername);
            if (userInfo != null && userInfo.length >= 3) {
                nameInput.setText(userInfo[0]);
                emailInput.setText(userInfo[1]);
                passInput.setText(userInfo[2]);
                confirmPassInput.setText(userInfo[2]);
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        }
        deleteAccountBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(Draweraccountinfo.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (oldUsername != null) {
                            boolean deleted = db.deleteUser(oldUsername);
                            if (deleted) {
                                // Clear user session
                                SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();

                                Toast.makeText(Draweraccountinfo.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                // Redirect to main/login activity
                                Intent mainIntent = new Intent(Draweraccountinfo.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Toast.makeText(Draweraccountinfo.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Draweraccountinfo.this, "No user logged in", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Enable editing
        editNameIcon.setOnClickListener(v -> enableEdit(nameInput));
        editEmailIcon.setOnClickListener(v -> enableEdit(emailInput));
        editPasswordIcon.setOnClickListener(v -> enableEdit(passInput));
        editConfirmPasswordIcon.setOnClickListener(v -> enableEdit(confirmPassInput));

        // Save updated info
        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passInput.getText().toString();
            String confirmPassword = confirmPassInput.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Draweraccountinfo.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isStrongPassword(password)) {
                Toast.makeText(Draweraccountinfo.this, "Password must be at least 8 characters long, and include uppercase, lowercase, number, and special character.", Toast.LENGTH_LONG).show();
                return;
            }


            if (db.updateUser(oldUsername, name, email, password)) {
                Toast.makeText(Draweraccountinfo.this, "Saved Successfully", Toast.LENGTH_SHORT).show();

                // Update SharedPreferences only if the username was changed
                if (!oldUsername.equals(name)) {
                    SharedPreferences.Editor userEditor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
                    userEditor.putString("username", name); // Save new username
                    userEditor.apply();
                    oldUsername = name; // Update the local variable
                }
            } else {
                Toast.makeText(Draweraccountinfo.this, "Error Saving Data", Toast.LENGTH_SHORT).show();
            }

        });

        // Navigation
        menu.setOnClickListener(v -> opendrawer(drawerlayout));
        home.setOnClickListener(v -> startActivity(new Intent(this, Dashboardwdrawer.class)));
        order.setOnClickListener(v -> startActivity(new Intent(this, Drawerorder.class)));
        contactus.setOnClickListener(v -> startActivity(new Intent(this, Drawercontactus.class)));
        termsandcondi.setOnClickListener(v -> startActivity(new Intent(this, Drawertermsandcondi.class)));
        notification.setOnClickListener(v -> startActivity(new Intent(this, Drawernotification.class)));

        logout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> logout())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void enableEdit(EditText field) {
        field.setEnabled(true);
        field.requestFocus();
        field.setSelection(field.getText().length());
    }

    public static void opendrawer(DrawerLayout drawerlayout) {
        drawerlayout.openDrawer(GravityCompat.START);
    }

    public static void closedrawer(DrawerLayout drawerlayout) {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closedrawer(drawerlayout);
    }
    private boolean isStrongPassword(String password) {
        // At least 8 characters, one digit, one upper, one lower, one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

}
