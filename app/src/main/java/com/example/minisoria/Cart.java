package com.example.minisoria;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.example.minisoria.model.Cartitem;

import java.util.ArrayList;
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
        setContentView(R.layout.cart);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        backbtn = findViewById(R.id.addtocartbckbtn);
        checkoutBtn = findViewById(R.id.Checkout);
        totalPriceText = findViewById(R.id.price);
        recyclerView = findViewById(R.id.cartview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getCartItems();  // Load cart items

        adapter = new CartAdapter(cartItems, true, this::updateSelectedTotalPrice, item -> {});
        recyclerView.setAdapter(adapter);

        backbtn.setOnClickListener(v -> finish());

        checkoutBtn.setOnClickListener(v -> {
            ArrayList<Cartitem> selectedItems = new ArrayList<>();
            ArrayList<String> imageUris = new ArrayList<>();

            for (Cartitem item : cartItems) {
                if (item.isChecked()) {
                    selectedItems.add(item);
                    if (item.getImageUri() != null) {
                        imageUris.add(item.getImageUri().toString());
                    } else {
                        imageUris.add(null);
                    }
                }
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Please select at least one item to checkout.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Cart.this, Checkout.class);
            intent.putExtra("selected_items", selectedItems);          // Pass Cartitems (Serializable)
            intent.putStringArrayListExtra("image_uris", imageUris);    // Pass image URIs as strings
            startActivity(intent);
        });

        updateSelectedTotalPrice();
    }

    private void updateSelectedTotalPrice() {
        float total = 0f;
        boolean hasSelected = false;
        for (Cartitem item : cartItems) {
            if (item.isChecked()) {
                hasSelected = true;
                try {
                    float price = Float.parseFloat(item.getPrice().replace("₱", "").trim());
                    total += price * item.getQuantity();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        totalPriceText.setText("₱" + String.format("%.2f", total));
        checkoutBtn.setEnabled(hasSelected);
    }
}
