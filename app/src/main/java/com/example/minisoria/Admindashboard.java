package com.example.minisoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.minisoria.db.DatabaseHelper;

public class Admindashboard extends AppCompatActivity {

    private ImageView menu;

    private DrawerLayout drawerlayout;
    private TextView logout;

    private LinearLayout order,acc;

    private CardView card, card1,card2;

    private TextView accountCount,textViewOrderCount;
    private DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admindashboard);



        drawerlayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menubtn);
        order = findViewById(R.id.Orderss);
        acc = findViewById(R.id.acc);
        card = findViewById(R.id.cardorder);
        card1=findViewById(R.id.cardaccount);
        card2=findViewById(R.id.cardaccesories);
        logout = findViewById(R.id.logout);
        accountCount = findViewById(R.id.textView1);
        textViewOrderCount = findViewById(R.id.textView17);
        db = new DatabaseHelper(this);
        updateAccountCount();
        updateOrderCount();



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendrawer(drawerlayout);
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admindashboard.this, Adminorder.class);
                startActivity(i);
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admindashboard.this, Adminaccounts.class);
                startActivity(i);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admindashboard.this, AdminAddProductActivity.class);
                startActivity(i);
            }
        });

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admindashboard.this, Adminaccounts.class);
                startActivity(i);

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admindashboard.this, Adminorder.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();
        });



    }
    private void updateOrderCount() {
        int orderCount = db.getOrderCount();
        textViewOrderCount = findViewById(R.id.textView17);
        textViewOrderCount.setText(String.valueOf(orderCount));
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admindashboard.this);
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


    private void logout() {
        Toast.makeText(Admindashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Admindashboard.this, MainActivity.class));
        finish();
    }
    private void updateAccountCount() {
        int count = 0;
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM users", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        accountCount.setText(String.valueOf(count));
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
    @Override
    protected void onResume() {
        super.onResume();
        updateAccountCount();
        updateOrderCount();
    }



}