package com.example.minisoria;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

public class Checkout extends AppCompatActivity {

    private ImageView backBtn;
    private Button placeOrderBtn;
    private TextView totalPriceText;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Cartitem> checkoutItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        backBtn = findViewById(R.id.addtocartbckbtn);
        placeOrderBtn = findViewById(R.id.Checkout);
        totalPriceText = findViewById(R.id.price);
        recyclerView = findViewById(R.id.cartview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Receive Cartitems
        Intent intent = getIntent();
        ArrayList<Cartitem> selectedItems = (ArrayList<Cartitem>) intent.getSerializableExtra("selected_items");

        // Receive image URIs separately
        ArrayList<String> imageUris = intent.getStringArrayListExtra("image_uris");

        if (selectedItems == null || selectedItems.isEmpty()) {
            Toast.makeText(this, "No items to display", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Restore imageUri field for each Cartitem from passed string URIs
        if (imageUris != null && imageUris.size() == selectedItems.size()) {
            for (int i = 0; i < selectedItems.size(); i++) {
                String uriString = imageUris.get(i);
                if (uriString != null) {
                    selectedItems.get(i).setImageUri(Uri.parse(uriString));
                } else {
                    selectedItems.get(i).setImageUri(null);
                }
            }
        }

        // Update list and adapter
        checkoutItems = selectedItems;
        adapter = new CartAdapter(checkoutItems, false, this::updateTotalPrice, item -> {});
        recyclerView.setAdapter(adapter);

        updateTotalPrice();

        backBtn.setOnClickListener(v -> finish());

        placeOrderBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Placeorder.class));
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Cartitem item : checkoutItems) {
            try {
                double price = Double.parseDouble(item.getPrice().replace("₱", "").trim());
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        totalPriceText.setText("₱" + String.format("%.2f", total));
    }
}
