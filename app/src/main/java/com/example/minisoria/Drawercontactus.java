package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Drawercontactus extends AppCompatActivity {

    private DrawerLayout drawerlayout;

    private LinearLayout home, order, notification, account, termsandcondi, contactus, logout;

    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawercontactus);

        menu = findViewById(R.id.menubtn);
        drawerlayout = findViewById(R.id.drawerlayout);
        home = findViewById(R.id.Home);
        account = findViewById(R.id.Account);
        notification = findViewById(R.id.Notif);
        order = findViewById(R.id.Order);
        contactus = findViewById(R.id.Contactus);
        termsandcondi = findViewById(R.id.termsandcondi);
        logout = findViewById(R.id.Logout);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendrawer(drawerlayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Drawercontactus.this, Dashboardwdrawer.class);
                startActivity(i);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Drawercontactus.this, Drawerorder.class);
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