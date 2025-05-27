package com.example.minisoria;

import static com.example.minisoria.Drawercontactus.opendrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.NotificationAdapter;
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class Drawernotification extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private LinearLayout home, order, account, termsandcondi, logout, contactus;
    private ImageView menu;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.drawernotification);

        menu = findViewById(R.id.menubtn);
        drawerlayout = findViewById(R.id.drawerlayout);
        home = findViewById(R.id.Home);
        account = findViewById(R.id.Account);
        order = findViewById(R.id.Order);
        termsandcondi = findViewById(R.id.termsandcondi);
        logout = findViewById(R.id.logoutt);
        contactus = findViewById(R.id.Contactus);
        TextView usernameText = findViewById(R.id.usersname);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        SharedPreferences userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = userPrefs.getString("username", null);

        if (username != null && !username.isEmpty()) {
            usernameText.setText(username);
        } else {
            usernameText.setText("No username found");
        }


        menu.setOnClickListener(v -> opendrawer(drawerlayout));

        home.setOnClickListener(v -> {
            Intent i = new Intent(Drawernotification.this, Dashboardwdrawer.class);
            startActivity(i);
        });

        order.setOnClickListener(v -> {
            Intent i = new Intent(Drawernotification.this, Drawerorder.class);
            startActivity(i);
        });

        contactus.setOnClickListener(v -> {
            Intent i = new Intent(Drawernotification.this, Drawercontactus.class);
            startActivity(i);
        });

        account.setOnClickListener(v -> {
            Intent i = new Intent(Drawernotification.this, Draweraccountinfo.class);
            startActivity(i);
        });

        termsandcondi.setOnClickListener(v -> {
            Intent i = new Intent(Drawernotification.this, Drawertermsandcondi.class);
            startActivity(i);
        });

        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();
        });

        // Setup RecyclerView and Adapter
        recyclerView = findViewById(R.id.notifdrawer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();

        if (username != null) {
            loadNotificationsForUser(username);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotificationsForUser(String username) {
        Cursor cursor = db.getNotificationsForUser(username);
        notificationList.clear();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                    notificationList.add(new Notification(title, message, time));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (adapter == null) {
            adapter = new NotificationAdapter(notificationList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Drawernotification.this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> logout())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout() {
        Toast.makeText(Drawernotification.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Drawernotification.this, MainActivity.class)); // Go to the login screen
        finish();
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
        // Clear the current list and reload notifications when the activity becomes visible again
        notificationList.clear();
        SharedPreferences userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = userPrefs.getString("username", null);
        if (username != null) {
            loadNotificationsForUser(username);
        }
    }

}
