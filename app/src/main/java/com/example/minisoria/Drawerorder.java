package com.example.minisoria;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Drawerorder extends AppCompatActivity {
    private DrawerLayout drawerlayout;
    private LinearLayout home, order, notification, account, termsandcondi, contacus, logout;

    private ImageView menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerorder);

        menu = findViewById(R.id.menubtn);

        home = findViewById(R.id.Home);
        account = findViewById(R.id.Account);
        notification = findViewById(R.id.Notif);
        order = findViewById(R.id.Order);
        contacus = findViewById(R.id.Contactus);
        termsandcondi = findViewById(R.id.termsandcondi);
        logout = findViewById(R.id.logoutt);
        drawerlayout = findViewById(R.id.drawerlayout);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendrawer(drawerlayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Drawerorder.this, Dashboardwdrawer.class);
                startActivity(i);
            }
        });
        contacus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Drawerorder.this, Drawercontactus.class);
                startActivity(i);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Drawerorder.this, Draweraccountinfo.class);
                startActivity(i);
            }
        });
        termsandcondi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Drawerorder.this, Drawertermsandcondi.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Drawerorder.this);
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
        Toast.makeText(Drawerorder.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Drawerorder.this, MainActivity.class));
        finish();  // Optionally, finish this activity to prevent going back
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

