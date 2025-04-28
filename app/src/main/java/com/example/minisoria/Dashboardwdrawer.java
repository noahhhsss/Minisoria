package com.example.minisoria;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class Dashboardwdrawer extends AppCompatActivity {

    private ImageView menu, addtocart;
    private Button acc, cari,food;
    private DrawerLayout drawerlayout;
    private LinearLayout home, order, notification, account, termsandcondi, contactus, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardwithdrawer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            ((android.view.Window) window).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        addtocart = findViewById(R.id.cart);
        acc = findViewById(R.id.accesories);
        cari = findViewById(R.id.caricature);
        food = findViewById(R.id.food);

        drawerlayout = findViewById(R.id.drawerlayout);

        home = findViewById(R.id.Home);
        account = findViewById(R.id.Account);
        notification = findViewById(R.id.Notif);
        order = findViewById(R.id.Order);
        contactus = findViewById(R.id.Contactus);
        termsandcondi = findViewById(R.id.termsandcondi);
        logout = findViewById(R.id.Logout);
        menu = findViewById(R.id.menubtn);


        FragmentManager fragmentManager = getSupportFragmentManager();

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, accesories.class, null).setReorderingAllowed(true).addToBackStack("name").commit();

            }
        });
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, caricature.class, null).setReorderingAllowed(true).addToBackStack("name").commit();

            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, food.class, null).setReorderingAllowed(true).addToBackStack("name").commit();

            }
        });


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboardwdrawer.this, Addtocart.class);
                startActivity(intent);
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendrawer(drawerlayout);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboardwdrawer.this, Drawercontactus.class);
                startActivity(i);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboardwdrawer.this, Drawerorder.class);
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
