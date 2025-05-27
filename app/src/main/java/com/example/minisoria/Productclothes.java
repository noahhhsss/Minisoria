package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.model.Cartitem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Productclothes extends AppCompatActivity {

    private ImageButton backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productclothes);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        backbtn.setOnClickListener(v -> finish());
        buyNowButton.setOnClickListener(v -> showBottomSheet(false));
        addToCartButton.setOnClickListener(v -> showBottomSheet(true));
    }

    private void showBottomSheet(final boolean isAddToCart) {
        int layoutRes = isAddToCart ? R.layout.popupaddtocartclothes : R.layout.productpopupclothes;
        View view = LayoutInflater.from(this).inflate(layoutRes, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        final TextView quantityText = view.findViewById(R.id.quantityText);
        final TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        final TextView increaseBtn = view.findViewById(R.id.increaseButton);
        final RadioGroup materialGroup = view.findViewById(R.id.materialGroup);
        final TextView priceText = view.findViewById(R.id.price);

        final RadioButton radioButton1 = view.findViewById(R.id.customizeBtn);
        final RadioButton radioButton2 = view.findViewById(R.id.cusBtn);

        radioButton1.setText("Per piece");
        radioButton2.setText("3 for 100");

        final int material1Price = 35;
        final int material2Price = 100;

        quantity = 1;
        quantityText.setText(String.valueOf(quantity));
        priceText.setText("₱0");
        materialGroup.clearCheck();

        materialGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.customizeBtn) {
                priceText.setText("₱" + material1Price);
            } else if (checkedId == R.id.cusBtn) {
                priceText.setText("₱" + material2Price);
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
                    Toast.makeText(Productclothes.this, "Please select a size", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedMaterialButton = view.findViewById(selectedId);
                String selectedMaterial = selectedMaterialButton.getText().toString();
                int unitPrice = selectedId == R.id.customizeBtn ? material1Price : material2Price;

                Cartitem item = new Cartitem(
                        "ClothesUser",
                        "Clothes",
                        String.valueOf(unitPrice),
                        quantity,
                        selectedMaterial,
                        R.drawable.clothes
                );
                CartManager.addToCart(item);
                Toast.makeText(Productclothes.this, "Added to cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

        } else {
            Button popupBuyNowButton = view.findViewById(R.id.buyNowButton);
            popupBuyNowButton.setOnClickListener(v -> {
                int selectedId = materialGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(Productclothes.this, "Please select a size", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedMaterialButton = view.findViewById(selectedId);
                String selectedMaterial = selectedMaterialButton.getText().toString();
                int unitPrice = selectedId == R.id.customizeBtn ? material1Price : material2Price;
                int totalPrice = unitPrice * quantity;

                Intent intent = new Intent(Productclothes.this, Cart.class);
                intent.putExtra("title", "Clothes");
                intent.putExtra("price", String.valueOf(unitPrice));
                intent.putExtra("totalPrice", String.valueOf(totalPrice));
                intent.putExtra("quantity", quantity);
                intent.putExtra("material", selectedMaterial);
                intent.putExtra("imageResId", R.drawable.clothes);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }
}
