package com.example.minisoria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        logout = findViewById(R.id.Logout);
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

