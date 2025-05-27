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

public class Producttotebag extends AppCompatActivity {

    private ImageView backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.producttotebag);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Producttotebag.this, Dashboardwdrawer.class);
                startActivity(i);
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
        int layoutRes;
        if (isAddToCart) {
            layoutRes = R.layout.popupaddtocarttotebag;
        } else {
            layoutRes = R.layout.productpopuptotebag;
        }

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

        // Prices for the two materials
        final int material1Price = 55;
        final int material2Price = 65;

        // Set default price text
        priceText.setText("₱" + material1Price);

        // Set texts of two radio buttons explicitly
        RadioButton radioButton1 = view.findViewById(R.id.customizeBtn);
        RadioButton radioButton2 = view.findViewById(R.id.cusBtn);

        radioButton1.setText("55 Pesos");
        radioButton2.setText("65 Pesos Additional design");

        // Clear any previous checked radio button
        materialGroup.clearCheck();

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
                        Toast.makeText(Producttotebag.this, "Please select a material", Toast.LENGTH_SHORT).show();
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
                            "TotebagUser",
                            "Totebag",
                            String.valueOf(unitPrice),
                            quantity,
                            selectedMaterial,
                            R.drawable.totebag
                    );

                    CartManager.addToCart(item);
                    Toast.makeText(Producttotebag.this, "Added to cart", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Producttotebag.this, "Please select a material", Toast.LENGTH_SHORT).show();
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

                    int totalPrice = unitPrice * quantity;

                    Intent intent = new Intent(Producttotebag.this, Cart.class);
                    intent.putExtra("title", "Totebag");
                    intent.putExtra("price", String.valueOf(unitPrice));
                    intent.putExtra("totalPrice", String.valueOf(totalPrice));
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("material", selectedMaterial);
                    intent.putExtra("imageResId", R.drawable.totebag);
                    startActivity(intent);

                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
    }
}
