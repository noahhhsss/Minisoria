package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.UserAdapter;
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.User;

import java.util.List;

public class Adminaccounts extends AppCompatActivity {

    private ImageView menu;
    private TextView logout;
    private DrawerLayout drawerlayout;
    private LinearLayout order, home;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private UserAdapter adapter;
    private List<User> userList;
    private DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminaccounts);

        // Initialize views
        drawerlayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menubtn);
        order = findViewById(R.id.Orderss);
        home = findViewById(R.id.Dashboard);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        // Database helper
        DB = new DatabaseHelper(this);

        // Load all users from DB
        userList = DB.getAllUsers();

        // Setup adapter with user action listeners for edit/delete
        adapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(int position) {
                User user = userList.get(position);
                showEditUserDialog(user, position);
            }

            @Override
            public void onDelete(int position) {
                User user = userList.get(position);
                confirmDeleteUser(user, position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Navigation drawer button
        menu.setOnClickListener(v -> opendrawer(drawerlayout));

        // Navigation drawer menu clicks
        home.setOnClickListener(v -> startActivity(new Intent(Adminaccounts.this, Admindashboard.class)));
        order.setOnClickListener(v -> startActivity(new Intent(Adminaccounts.this, Adminorder.class)));

        // Logout click
        logout.setOnClickListener(v -> {
            Log.d("Drawer", "Logout button clicked");
            showLogoutDialog();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void confirmDeleteUser(User user, int position) {
        new AlertDialog.Builder(Adminaccounts.this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean deleted = DB.deleteUser(user.getName());
                    if (deleted) {
                        userList.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(Adminaccounts.this, "Deleted user: " + user.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Adminaccounts.this, "Failed to delete user.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditUserDialog(User user, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit User");

        View dialogView = getLayoutInflater().inflate(R.layout.adminedituser, null);
        EditText usernameInput = dialogView.findViewById(R.id.editUsername);
        EditText emailInput = dialogView.findViewById(R.id.editEmail);
        EditText passwordInput = dialogView.findViewById(R.id.editPassword);


        usernameInput.setText(user.getName());
        emailInput.setText(user.getEmail());
        passwordInput.setText(user.getPassword());

        builder.setView(dialogView);

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newUsername = usernameInput.getText().toString().trim();
            String newEmail = emailInput.getText().toString().trim();
            String newPassword = passwordInput.getText().toString().trim();

            if (newUsername.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(Adminaccounts.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = DB.updateUser(user.getName(), newUsername, newEmail, newPassword);
            if (updated) {
                user.setName(newUsername);
                user.setEmail(newEmail);
                user.setPassword(newPassword);
                adapter.notifyItemChanged(position);
                Toast.makeText(Adminaccounts.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(Adminaccounts.this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(Adminaccounts.this)
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> logout())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    private void logout() {
        Toast.makeText(Adminaccounts.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Adminaccounts.this, MainActivity.class));
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
