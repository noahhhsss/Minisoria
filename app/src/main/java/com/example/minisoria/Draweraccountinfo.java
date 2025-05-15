package com.example.minisoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout home, order, notification, termsandcondi, contactus,logout;
    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draweraccountinfo);

        db = new DatabaseHelper(this);

        // Drawer and nav setup
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

        // Drawer menu
        menu.setOnClickListener(v -> opendrawer(drawerlayout));

        // Enable editing on icon
        editNameIcon.setOnClickListener(v -> enableEdit(nameInput));
        editEmailIcon.setOnClickListener(v -> enableEdit(emailInput));
        editPasswordIcon.setOnClickListener(v -> enableEdit(passInput));
        editConfirmPasswordIcon.setOnClickListener(v -> enableEdit(confirmPassInput));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Draweraccountinfo.this, Dashboardwdrawer.class);
                startActivity(i);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Draweraccountinfo.this, Drawerorder.class);
                startActivity(i);
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Draweraccountinfo.this, Drawercontactus.class);
                startActivity(i);
            }
        });
        termsandcondi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Draweraccountinfo.this, Drawertermsandcondi.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();  // Show the dialog before logout
        });


        // Save button functionality
        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passInput.getText().toString();
            String confirmPassword = confirmPassInput.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Draweraccountinfo.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean insert = db.insertUser(name, email, password);
            if (insert) {
                Toast.makeText(Draweraccountinfo.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Draweraccountinfo.this, "Error Saving Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Draweraccountinfo.this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Logout function
    private void logout() {
        Toast.makeText(Draweraccountinfo.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Draweraccountinfo.this, MainActivity.class)); // Go to the login screen
        finish();  // Optionally, finish this activity to prevent going back
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
}
