package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.model.Cartitem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Productkeychain extends AppCompatActivity {

    private ImageView backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productkeychain);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Productkeychain.this, Dashboardwdrawer.class);
            startActivity(intent);
        });

        buyNowButton.setOnClickListener(v -> showBottomSheet(false));
        addToCartButton.setOnClickListener(v -> showBottomSheet(true));
    }

    private void showBottomSheet(boolean isAddToCart) {
        int layoutRes = isAddToCart ? R.layout.popupaddtocartkeychain : R.layout.productpopupkeychain;
        View view = LayoutInflater.from(this).inflate(layoutRes, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        TextView quantityText = view.findViewById(R.id.quantityText);
        TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        TextView increaseBtn = view.findViewById(R.id.increaseButton);

        quantityText.setText(String.valueOf(quantity));

        increaseBtn.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        decreaseBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        int unitPrice = 15;

        if (isAddToCart) {
            Button popupAddToCartButton = view.findViewById(R.id.addtocart);
            popupAddToCartButton.setOnClickListener(v -> {
                int qty = Integer.parseInt(quantityText.getText().toString());

                Cartitem item = new Cartitem(
                        "KeychainUser",              // owner
                        "Keychain",                  // title
                        String.valueOf(unitPrice),   // unit price
                        qty,                         // quantity
                        "",                          // no material
                        R.drawable.keychain          // image
                );

                CartManager.addToCart(item);
                Toast.makeText(Productkeychain.this, "Added to cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

        } else {
            Button popupBuyNowButton = view.findViewById(R.id.buyNowButton);
            popupBuyNowButton.setOnClickListener(v -> {
                int qty = Integer.parseInt(quantityText.getText().toString());
                int totalPrice = unitPrice * qty;

                Intent intent = new Intent(Productkeychain.this, Cart.class);
                intent.putExtra("title", "Keychain");
                intent.putExtra("price", String.valueOf(unitPrice));
                intent.putExtra("totalPrice", String.valueOf(totalPrice));
                intent.putExtra("quantity", qty);
                intent.putExtra("material", ""); // no material
                intent.putExtra("imageResId", R.drawable.keychain);
                startActivity(intent);

                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }
}
