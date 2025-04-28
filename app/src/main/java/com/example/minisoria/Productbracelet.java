package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Productbracelet extends AppCompatActivity {

    private ImageView cart, backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productbracelet);

        cart = findViewById(R.id.addtocart);
        backbtn = findViewById(R.id.backbtn);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Productbracelet.this, Addtocart.class);
                startActivity(i);
            }
        });




        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Productbracelet.this, Dashboardwdrawer.class);
                startActivity(i);
            }
        });

    }
}
