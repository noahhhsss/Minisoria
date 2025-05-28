package com.example.minisoria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private LinearLayout addToCartButton;
    private Button buyNowButton;
    private Product product;

    private static final List<String> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productkeychain);

        ImageView productImage = findViewById(R.id.productImage);
        TextView productPriceView = findViewById(R.id.productPrice);
        TextView productNameView = findViewById(R.id.productName);
        addToCartButton = findViewById(R.id.addtocart);
        buyNowButton = findViewById(R.id.buyNowButton);
        TextView productDescriptionView = findViewById(R.id.productDescription);

        Intent intent = getIntent();
        product = intent.getParcelableExtra("product");

        if (product != null) {
            String name = product.getName();
            String priceString = String.format("₱%.2f", product.getPrice());
            String description = product.getDescription();
            String imageUriString = product.getImageUri();

            productNameView.setText(name);
            productPriceView.setText(priceString);
            productDescriptionView.setText(description);

            if (imageUriString != null && !imageUriString.isEmpty()) {
                Uri imageUri = Uri.parse(imageUriString);
                try {
                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                productImage.setImageURI(imageUri);
            } else {
                productImage.setImageResource(R.drawable.keychain); // fallback image
            }

            addToCartButton.setOnClickListener(v -> showPopup(product, false, R.layout.productpopupaddtocart));
            buyNowButton.setOnClickListener(v -> showPopup(product, true, R.layout.productpopup));


        } else {
            Toast.makeText(this, "Product data is missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showPopup(Product product, boolean isBuyNow, int layoutResId) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(layoutResId, null);
        dialog.setContentView(view);

        ImageView popupImage = view.findViewById(R.id.popupProductImage);
        TextView popupPrice = view.findViewById(R.id.popupProductPrice);
        TextView quantityText = view.findViewById(R.id.quantityText);
        TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        TextView increaseBtn = view.findViewById(R.id.increaseButton);

        // Different confirm button IDs based on layout
        Button confirmBtn;
        if (isBuyNow) {
            confirmBtn = view.findViewById(R.id.buyNowButton);
        } else {
            confirmBtn = view.findViewById(R.id.addtocart);
        }

        popupPrice.setText(String.format("₱%.2f", product.getPrice()));

        if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            Uri imageUri = Uri.parse(product.getImageUri());
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            popupImage.setImageURI(imageUri);
        }

        final int[] quantity = {1};
        quantityText.setText(String.valueOf(quantity[0]));

        decreaseBtn.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.valueOf(quantity[0]));
            }
        });

        increaseBtn.setOnClickListener(v -> {
            quantity[0]++;
            quantityText.setText(String.valueOf(quantity[0]));
        });

        confirmBtn.setOnClickListener(v -> {
            String item = product.getName() + " - ₱" + product.getPrice() + " x" + quantity[0];
            cart.add(item);

            if (isBuyNow) {
                Toast.makeText(this, "Buying: " + item, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, item + " added to cart", Toast.LENGTH_SHORT).show();
            }

            printCartContents();
            dialog.dismiss();
        });

        dialog.show();
    }



    private void printCartContents() {
        Log.d("CART CONTENTS", "Current Cart Items:");
        for (String item : cart) {
            Log.d("CART CONTENTS", item);
        }
    }
}
