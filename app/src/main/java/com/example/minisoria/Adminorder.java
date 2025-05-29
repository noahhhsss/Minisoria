package com.example.minisoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.AdminOrderAdapter;
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.Order;

import java.util.List;

public class Adminorder extends AppCompatActivity {

    private ImageView menu;
    private TextView logout;
    private DrawerLayout drawerlayout;
    private List<Order> orders;

    private LinearLayout home, acc;
    private RecyclerView recyclerView;
    private AdminOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminorder);

        drawerlayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menubtn);
        home = findViewById(R.id.Dashboard);
        acc = findViewById(R.id.acc);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.ordersRecyclerView);

        menu.setOnClickListener(v -> opendrawer(drawerlayout));

        home.setOnClickListener(v -> {
            Intent i = new Intent(Adminorder.this, Admindashboard.class);
            startActivity(i);
        });

        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        orders = dbHelper.getAllOrders();

        adapter = new AdminOrderAdapter(this, orders, new AdminOrderAdapter.OnOrderActionListener() {
            @Override
            public void onAccept(Order order, int position) {
                DatabaseHelper db = new DatabaseHelper(Adminorder.this);
                int userId = order.getUserId();       // Assuming Order model has getUserId()
                int orderId = order.getOrderId();
                String username = order.getUserName(); // ✅ Fix: define username from order

                boolean updated = db.updateOrderStatusById(orderId, "accepted");
                if (updated) {
                    db.insertNotification(username, "Order has been proccesed", "Your order " + orderId + " has been proccesed you may now go to our booth to collect.");
                    order.setStatus("accepted");
                    adapter.notifyItemChanged(position);
                    Toast.makeText(Adminorder.this, "Order accepted and notification sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Adminorder.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDelete(Order order, int position) {
                DatabaseHelper db = new DatabaseHelper(Adminorder.this);
                boolean deleted = db.deleteOrder(order.getUserName(), order.getTitle());
                if (deleted) {
                    int actualPosition = orders.indexOf(order);
                    if (actualPosition != -1) {
                        orders.remove(actualPosition);
                        adapter.notifyItemRemoved(actualPosition);
                    }
                    Toast.makeText(Adminorder.this, "Order deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Adminorder.this, "Failed to delete order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Adminorder.this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> logout())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout() {
        Toast.makeText(Adminorder.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Adminorder.this, MainActivity.class));
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
}
