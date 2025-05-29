package com.example.minisoria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.model.Cartitem;
import com.example.minisoria.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private LinearLayout addToCartButton, attachimage;
    private Button buyNowButton;

    private Product product;
    private ImageView btn;

    private static final List<String> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productkeychain);

        btn=findViewById(R.id.backbtn);
        ImageView productImage = findViewById(R.id.productImage);
        TextView productPriceView = findViewById(R.id.productPrice);
        TextView productNameView = findViewById(R.id.productName);
        addToCartButton = findViewById(R.id.addtocart);
        buyNowButton = findViewById(R.id.buyNowButton);
        attachimage = findViewById(R.id.messageButton);
        TextView productDescriptionView = findViewById(R.id.productDescription);

        Intent intent = getIntent();
        product = intent.getParcelableExtra("product");

        attachimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetail.this, Activitysubmitimage.class);
                startActivity(i);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                productImage.setImageResource(R.drawable.keychain);
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
        RadioGroup materialGroup = view.findViewById(R.id.materialGroup);

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

        List<String> materials = product.getMaterials();
        List<Double> prices = product.getMaterialPrices();

        materialGroup.removeAllViews();

        if (materials != null && prices != null &&
                !materials.isEmpty() && !prices.isEmpty() &&
                materials.size() == prices.size()) {

            popupPrice.setText(String.format("₱%.2f", prices.get(0)));

            for (int i = 0; i < materials.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(materials.get(i) + " (₱" + prices.get(i) + ")");
                radioButton.setTextSize(15);
                radioButton.setTextColor(getResources().getColor(R.color.black));
                radioButton.setPadding(24, 16, 24, 16);
                radioButton.setBackground(getDrawable(R.drawable.cornerradius));
                radioButton.setButtonDrawable(R.drawable.dropdown);

                int index = i;
                radioButton.setOnClickListener(v -> {
                    popupPrice.setText(String.format("₱%.2f", prices.get(index)));
                });

                materialGroup.addView(radioButton);

                if (i == 0) radioButton.setChecked(true);
            }

        } else {
            // fallback if materials/prices are missing or invalid
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Default Material");
            radioButton.setTextSize(15);
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setPadding(24, 16, 24, 16);
            radioButton.setBackground(getDrawable(R.drawable.cornerradius));
            radioButton.setButtonDrawable(R.drawable.dropdown);
            radioButton.setChecked(true);
            materialGroup.addView(radioButton);

            popupPrice.setText(String.format("₱%.2f", product.getPrice()));

            Log.d("DEBUG", "Materials passed to popup: " + product.getMaterials());
            Log.d("DEBUG", "Prices passed to popup: " + product.getMaterialPrices());
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

        final Product finalProduct = product;  // Make product final for use in listener

        confirmBtn.setOnClickListener(v -> {
            try {
                String selectedMaterial = null;
                double selectedPrice = 0;
                int checkedRadioButtonId = materialGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = materialGroup.findViewById(checkedRadioButtonId);
                    selectedMaterial = selectedRadioButton.getText().toString();
                    int openParen = selectedMaterial.lastIndexOf('₱');
                    int closeParen = selectedMaterial.lastIndexOf(')');
                    if (openParen != -1 && closeParen != -1 && closeParen > openParen) {
                        String priceStr = selectedMaterial.substring(openParen + 1, closeParen);
                        try {
                            selectedPrice = Double.parseDouble(priceStr);
                        } catch (NumberFormatException e) {
                            selectedPrice = finalProduct.getPrice();
                        }
                    }
                }

                int finalQuantity = quantity[0];

                if (!isBuyNow) {
                    // Build the Cartitem directly
                    Cartitem newItem = new Cartitem(
                            "defaultUser", // or retrieve from SharedPreferences
                            finalProduct.getName(),
                            String.format("₱%.2f", selectedPrice),
                            finalQuantity,
                            selectedMaterial,
                            R.drawable.keychain, // Or appropriate resource ID if needed
                            Uri.parse(finalProduct.getImageUri())

                    );

                    CartManager.addToCart(newItem);


                    Toast.makeText(ProductDetail.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Cartitem newItem = new Cartitem(
                            "defaultUser", // Replace with SharedPreferences if needed
                            finalProduct.getName(),
                            String.format("₱%.2f", selectedPrice),
                            finalQuantity,
                            selectedMaterial,
                            R.drawable.keychain,
                            Uri.parse(finalProduct.getImageUri())
                    );

                    CartManager.addToCart(newItem);

                    Intent intent = new Intent(ProductDetail.this, Checkout.class);
                    startActivity(intent);
                    Toast.makeText(ProductDetail.this, "Proceeding to buy", Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(ProductDetail.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });


        dialog.show();
    }
}
