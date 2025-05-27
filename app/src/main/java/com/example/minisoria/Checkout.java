package com.example.minisoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.example.minisoria.CartManager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.CartAdapter;
import com.example.minisoria.model.Cartitem;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private ImageView backBtn;
    private Button checkoutBtn;

    private TextView priceTextView;
    private RecyclerView recyclerView;
    private CartAdapter adapter;

    private List<Cartitem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            ((android.view.Window) window).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        backBtn = findViewById(R.id.addtocartbckbtn);
        checkoutBtn = findViewById(R.id.Checkout);
        priceTextView = findViewById(R.id.price);
        recyclerView = findViewById(R.id.cartview);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, true, this::updateTotalPrice);

        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkoutBtn.setOnClickListener(v -> {
            List<Cartitem> checkedItems = new ArrayList<>();
            for (Cartitem item : cartItems) {
                if (item.isChecked() && item.getQuantity() > 0 && item.getTitle() != null && !item.getTitle().isEmpty()) {
                    checkedItems.add(item);
                }
            }

            if (checkedItems.isEmpty()) {
                Toast.makeText(this, "No products selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Order placed for " + checkedItems.size() + " items", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Checkout.this, Cart.class));
            }
        });



        handleIntent(getIntent());

        loadCartItemsAndUpdateUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
        loadCartItemsAndUpdateUI();
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("title")) {
            String title = intent.getStringExtra("title");
            String price = intent.getStringExtra("price");
            String material = intent.getStringExtra("material");
            int quantity = intent.getIntExtra("quantity", 1);
            int imageResId = intent.getIntExtra("imageResId", R.drawable.headbands);

            Log.d("CheckoutHandleIntent", "title=" + title + ", material=" + material + ", quantity=" + quantity);

            SharedPreferences userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String username = userPrefs.getString("username", "defaultUser");

            Cartitem newItem = new Cartitem(username, title, price, quantity, material, imageResId);
            CartManager.addToCart(newItem);
            adapter.notifyDataSetChanged();
        }
    }


    private void loadCartItemsAndUpdateUI() {
        cartItems.clear();
        cartItems.addAll(CartManager.getCartItems());
        adapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Cartitem item : cartItems) {
            try {
                double price = Double.parseDouble(item.getPrice());
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        priceTextView.setText(String.format("%.2f", total));
    }
}
