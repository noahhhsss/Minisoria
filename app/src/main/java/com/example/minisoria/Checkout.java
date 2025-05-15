package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.CartAdapter;
import com.example.minisoria.model.Cartitem;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private ImageView backBtn;
    RecyclerView recyclerView;
    CartAdapter adapter;
    List<Cartitem> cartItems;
    Button btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        backBtn = findViewById(R.id.addtocartbckbtn);
        btn = findViewById(R.id.Checkout);
        recyclerView = findViewById(R.id.cartview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        cartItems = new ArrayList<>();
        cartItems.add(new Cartitem("Cookie", "P69", "P123", 1, R.drawable.contact));

        adapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Checkout.this, Addtocart.class);
                startActivity(i);
            }
        });
    }
}