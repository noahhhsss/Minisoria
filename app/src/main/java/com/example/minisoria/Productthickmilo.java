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

public class Productthickmilo extends AppCompatActivity {

    private ImageButton backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productthickmilo);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(false);
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(true);
            }
        });
    }

    private void showBottomSheet(final boolean isAddToCart) {
        int layoutRes = isAddToCart ? R.layout.popupaddtocartthickmilo : R.layout.productpopupthickmilo;
        View view = LayoutInflater.from(this).inflate(layoutRes, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        final TextView quantityText = view.findViewById(R.id.quantityText);
        final TextView decreaseBtn = view.findViewById(R.id.decreaseButton);
        final TextView increaseBtn = view.findViewById(R.id.increaseButton);
        final RadioGroup materialGroup = view.findViewById(R.id.materialGroup);
        final TextView priceText = view.findViewById(R.id.price);

        quantity = 1;
        quantityText.setText(String.valueOf(quantity));

        final int material1Price = 15;
        final int material2Price = 25;

        RadioButton radioButton1 = view.findViewById(R.id.customizeBtn);
        RadioButton radioButton2 = view.findViewById(R.id.cusBtn);

        radioButton1.setText("6.5oz");
        radioButton2.setText("12oz");

        materialGroup.clearCheck();

        priceText.setText("₱0");

        materialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.customizeBtn) {
                    priceText.setText("₱" + material1Price);
                } else if (checkedId == R.id.cusBtn) {
                    priceText.setText("₱" + material2Price);
                }
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityText.setText(String.valueOf(quantity));
                }
            }
        });

        if (isAddToCart) {
            Button popupAddToCartButton = view.findViewById(R.id.addtocart);
            popupAddToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = materialGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(Productthickmilo.this, "Please select a material", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RadioButton selectedMaterialButton = view.findViewById(selectedId);
                    String selectedMaterial = selectedMaterialButton.getText().toString();
                    int unitPrice = 0;
                    if (selectedId == R.id.customizeBtn) {
                        unitPrice = material1Price;
                    } else if (selectedId == R.id.cusBtn) {
                        unitPrice = material2Price;
                    }
                    Cartitem item = new Cartitem(
                            "ThickMiloUser",
                            "Thick Milo",
                            String.valueOf(unitPrice),
                            quantity,
                            selectedMaterial,
                            R.drawable.thickmilo
                    );
                    CartManager.addToCart(item);
                    Toast.makeText(Productthickmilo.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            });
        } else {
            Button popupBuyNowButton = view.findViewById(R.id.buyNowButton);
            popupBuyNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = materialGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(Productthickmilo.this, "Please select a material", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RadioButton selectedMaterialButton = view.findViewById(selectedId);
                    String selectedMaterial = selectedMaterialButton.getText().toString();
                    int unitPrice = 0;
                    if (selectedId == R.id.customizeBtn) {
                        unitPrice = material1Price;
                    } else if (selectedId == R.id.cusBtn) {
                        unitPrice = material2Price;
                    }
                    Intent intent = new Intent(Productthickmilo.this, Cart.class);
                    intent.putExtra("title", "Thick Milo");
                    intent.putExtra("price", String.valueOf(unitPrice));
                    intent.putExtra("totalPrice", String.valueOf(unitPrice)); // stays the same
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("material", selectedMaterial);
                    intent.putExtra("imageResId", R.drawable.thickmilo);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
    }
}
