package com.example.minisoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.CartAdapter;
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.Cartitem;

import java.util.List;

public class Cart extends AppCompatActivity {

    private ImageView backbtn;
    private Button checkoutBtn;
    private TextView totalPriceText;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Cartitem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            ((android.view.Window) window).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        backbtn = findViewById(R.id.addtocartbckbtn);
        checkoutBtn = findViewById(R.id.Checkout);
        totalPriceText = findViewById(R.id.price);
        recyclerView = findViewById(R.id.cartview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getCartItems();

        adapter = new CartAdapter(cartItems, false, this::updateTotalPrice);
        recyclerView.setAdapter(adapter);



        handleIntent(getIntent());

        backbtn.setOnClickListener(v -> finish());

        checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                // Get logged-in username
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String username = prefs.getString("username", "defaultUser");

                DatabaseHelper dbHelper = new DatabaseHelper(this);

                // Save each cart item as an order with username
                for (Cartitem item : cartItems) {
                    item.setUsername(username); // ✅ Set username here
                    dbHelper.insertOrder(item);
                }

                CartManager.clearCart();

                // Navigate to PlaceOrder activity to show order confirmation
                startActivity(new Intent(Cart.this, Placeorder.class));
                finish();
            }
        });

        updateTotalPrice();
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("title")) {
            String title = intent.getStringExtra("title");

            String price = intent.getStringExtra("price");
            String material = intent.getStringExtra("material");
            int quantity = intent.getIntExtra("quantity", 1);
            String imageUriString = intent.getStringExtra("imageUri");

            int imageResId = R.drawable.keychain;
            Uri imageUri = null;
            if (imageUriString != null && !imageUriString.isEmpty()) {
                imageUri = Uri.parse(imageUriString);
            }
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String username = prefs.getString("username", "defaultUser");

            Cartitem newItem = new Cartitem(username, title, price, quantity, material, imageResId, imageUri);
            CartManager.addToCart(newItem);
            adapter.notifyDataSetChanged();
        }
    }




    private void updateTotalPrice() {
        double total = 0;
        for (Cartitem item : cartItems) {
            try {
                double price = Double.parseDouble(item.getPrice().replace("₱", ""));
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        totalPriceText.setText("₱" + String.format("%.2f", total));
    }
}
