package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Categories extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);


        CardView cardAccessories = findViewById(R.id.cardAccessories);
        CardView cardCaricature = findViewById(R.id.cardCaricature);
        CardView cardFood = findViewById(R.id.cardFood);

        cardAccessories.setOnClickListener(v -> {
            Intent intent = new Intent(Categories.this, AdminAddProductActivity.class);
            startActivity(intent);
        });

        cardCaricature.setOnClickListener(v -> {
            Intent intent = new Intent(Categories.this, Adminaddproductaccessories.class);
            startActivity(intent);
        });

        cardFood.setOnClickListener(v -> {
            Intent intent = new Intent(Categories.this, AdminAddProductActivity.class);
            startActivity(intent);
        });

    }
}

