package com.example.minisoria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Productthickmilo extends AppCompatActivity {
    private ImageView cart, backbtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productthickmilo);

        cart = findViewById(R.id.addtocart);
        backbtn = findViewById(R.id.backbtn);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Productthickmilo.this, Addtocart.class);
                startActivity(i);
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Productthickmilo.this, Dashboardwdrawer.class);
                startActivity(i);
            }
        });
    }
}
