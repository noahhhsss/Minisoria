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
        setContentView(R.layout.productclothes);  // your clothes layout

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        backbtn.setOnClickListener(v -> finish());  // or navigate back

        buyNowButton.setOnClickListener(v -> showBottomSheet(false));
        addToCartButton.setOnClickListener(v -> showBottomSheet(true));
    }

    private void showBottomSheet(boolean isAddToCart) {
        quantity = 1;  // reset quantity on each popup open

        int layoutRes = isAddToCart ? R.layout.popupaddtocartclothes : R.layout.productpopupclothes;
        View view = LayoutInflater.from(this).inflate(layoutRes, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        TextView quantityText = view.findViewById(R.id.quantityText);
        TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        TextView increaseBtn = view.findViewById(R.id.increaseButton);

        RadioGroup materialGroup = view.findViewById(R.id.materialGroup);  // optional, if in layout

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

        int unitPrice = 55;

        if (isAddToCart) {
            Button popupAddToCartButton = view.findViewById(R.id.addtocart);
            popupAddToCartButton.setOnClickListener(v -> {
                int qty = Integer.parseInt(quantityText.getText().toString());

                // Get selected material text or empty if none selected
                String material = "";
                if (materialGroup != null) {
                    int selectedId = materialGroup.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        RadioButton selectedRadio = view.findViewById(selectedId);
                        material = selectedRadio.getText().toString();
                    }
                }

                Cartitem item = new Cartitem(
                        "ClothesUser",
                        "Clothes",
                        String.valueOf(unitPrice),
                        qty,
                        material,
                        R.drawable.clothes
                );

                CartManager.addToCart(item);
                Toast.makeText(Productclothes.this, "Added to cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

        } else {
            Button popupBuyNowButton = view.findViewById(R.id.buyNowButton);
            popupBuyNowButton.setOnClickListener(v -> {
                int qty = Integer.parseInt(quantityText.getText().toString());
                int totalPrice = unitPrice * qty;

                String material = "";
                if (materialGroup != null) {
                    int selectedId = materialGroup.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        RadioButton selectedRadio = view.findViewById(selectedId);
                        material = selectedRadio.getText().toString();
                    }
                }

                Intent intent = new Intent(Productclothes.this, Cart.class);
                intent.putExtra("title", "Clothes");
                intent.putExtra("price", String.valueOf(unitPrice));
                intent.putExtra("totalPrice", String.valueOf(totalPrice));
                intent.putExtra("quantity", qty);
                intent.putExtra("material", material);
                intent.putExtra("imageResId", R.drawable.clothes);
                startActivity(intent);

                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }
}
