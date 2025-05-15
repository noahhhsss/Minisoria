package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.CartAdapter;
import com.example.minisoria.model.Cartitem;

import java.util.ArrayList;
import java.util.List;

public class Addtocart extends AppCompatActivity {

    private ImageView backBtn;
    RecyclerView recyclerView;
    CartAdapter adapter;
    List<Cartitem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.checkout);

        backBtn = findViewById(R.id.addtocartbckbtn);
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
                finish(); // go back to previous activity
            }
        });
    }
}
