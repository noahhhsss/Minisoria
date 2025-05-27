package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.model.Cartitem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Map;

public class Productbracelet extends AppCompatActivity {

    private ImageView backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton;
    private int quantity = 1;

    // Prices for each package
    private final Map<String, Integer> materialPrices = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productbracelet);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        // Set prices for each package
        materialPrices.put("Package 1", 40);
        materialPrices.put("Package 2", 45);
        materialPrices.put("Package 3", 50);

        backbtn.setOnClickListener(v -> {
            Intent i = new Intent(Productbracelet.this, Dashboardwdrawer.class);
            startActivity(i);
        });

        buyNowButton.setOnClickListener(v -> showBottomSheet(false));
        addToCartButton.setOnClickListener(v -> showBottomSheet(true));
    }

    private void showBottomSheet(boolean isAddToCart) {
        int layoutRes = isAddToCart ? R.layout.popupaddtocartbracelet : R.layout.productpopupbracelet;
        View view = LayoutInflater.from(this).inflate(layoutRes, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        TextView quantityText = view.findViewById(R.id.quantityText);
        TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        TextView increaseBtn = view.findViewById(R.id.increaseButton);
        RadioGroup materialGroup = view.findViewById(R.id.materialGroup);
        TextView priceText = view.findViewById(R.id.price);  // The price TextView in popup

        quantity = 1; // Reset quantity each time popup opens
        quantityText.setText(String.valueOf(quantity));

        // Set default price display (Package 1)
        priceText.setText("₱" + materialPrices.get("Package 1"));

        // Update price when package selected
        materialGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = view.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String selectedPackage = selectedRadioButton.getText().toString();
                Integer price = materialPrices.get(selectedPackage);
                if (price != null) {
                    priceText.setText("₱" + price);
                }
            }
        });

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

        if (isAddToCart) {
            Button popupAddToCartButton = view.findViewById(R.id.addtocart);
            popupAddToCartButton.setOnClickListener(v -> {
                int selectedId = materialGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(Productbracelet.this, "Please select a package", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedMaterialButton = view.findViewById(selectedId);
                String selectedMaterial = selectedMaterialButton.getText().toString();

                int unitPrice = materialPrices.containsKey(selectedMaterial)
                        ? materialPrices.get(selectedMaterial)
                        : 0;

                Cartitem item = new Cartitem(
                        "BraceletUser",
                        "Bracelet",
                        String.valueOf(unitPrice),
                        quantity,
                        selectedMaterial,
                        R.drawable.bracelet
                );

                CartManager.addToCart(item);
                Toast.makeText(Productbracelet.this, "Added to cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

        } else {
            Button popupBuyNowButton = view.findViewById(R.id.buyNowButton);
            popupBuyNowButton.setOnClickListener(v -> {
                int selectedId = materialGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(Productbracelet.this, "Please select a package", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedMaterialButton = view.findViewById(selectedId);
                String selectedMaterial = selectedMaterialButton.getText().toString();

                int unitPrice = materialPrices.containsKey(selectedMaterial)
                        ? materialPrices.get(selectedMaterial)
                        : 0;

                Intent intent = new Intent(Productbracelet.this, Cart.class);
                intent.putExtra("title", "Bracelet");
                intent.putExtra("price", String.valueOf(unitPrice));
                intent.putExtra("quantity", quantity);
                intent.putExtra("material", selectedMaterial);
                intent.putExtra("imageResId", R.drawable.bracelet);
                startActivity(intent);

                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }
}
