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

import com.example.minisoria.model.Cartitem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class product extends AppCompatActivity {

    private ImageView backbtn;
    private Button buyNowButton;
    private LinearLayout addToCartButton, mess;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.product);

        backbtn = findViewById(R.id.backbtn);
        buyNowButton = findViewById(R.id.buyNowButton);
        addToCartButton = findViewById(R.id.addtocart);
        mess = findViewById(R.id.msg);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(product.this, Dashboardwdrawer.class);
                startActivity(intent);
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
        int layoutResource;
        if (isAddToCart) {
            layoutResource = R.layout.productpopupaddtocart;
        } else {
            layoutResource = R.layout.productpopup;
        }

        View bottomSheetView = LayoutInflater.from(this).inflate(layoutResource, null);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        final TextView quantityText = bottomSheetView.findViewById(R.id.quantityText);
        final TextView decreaseButton = bottomSheetView.findViewById(R.id.decreaseButton);
        final TextView increaseButton = bottomSheetView.findViewById(R.id.increaseButton);
        final RadioGroup materialGroup = bottomSheetView.findViewById(R.id.materialGroup);
        final TextView priceText = bottomSheetView.findViewById(R.id.price);

        quantity = 1;
        quantityText.setText(String.valueOf(quantity));

        // Initialize prices for the two materials
        final int material1Price = 20;
        final int material2Price = 25;

        // Set default price on load (Material 1 price)
        priceText.setText("₱" + material1Price);

        // Set RadioButtons texts and IDs dynamically
        RadioButton radioButton1 = bottomSheetView.findViewById(R.id.customizeBtn);
        RadioButton radioButton2 = bottomSheetView.findViewById(R.id.cusBtn);

        radioButton1.setText("Fabric scraps");
        radioButton2.setText("Base");

        // Clear any checked buttons
        materialGroup.clearCheck();

        // Listener for radio buttons to update price text
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

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityText.setText(String.valueOf(quantity));
                }
            }
        });

        if (isAddToCart) {
            Button addToCartBtn = bottomSheetView.findViewById(R.id.addtocart);
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(quantityText.getText().toString());

                    int selectedRadioId = materialGroup.getCheckedRadioButtonId();
                    if (selectedRadioId == -1) {
                        Toast.makeText(product.this, "Please select a material", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RadioButton selectedRadio = bottomSheetView.findViewById(selectedRadioId);
                    String selectedMaterial = selectedRadio.getText().toString();

                    int unitPrice = 0;
                    if (selectedRadioId == R.id.customizeBtn) {
                        unitPrice = material1Price;
                    } else if (selectedRadioId == R.id.cusBtn) {
                        unitPrice = material2Price;
                    }

                    Cartitem item = new Cartitem(
                            "HeadbandUser",
                            "Headband",
                            String.valueOf(unitPrice),
                            qty,
                            selectedMaterial,
                            R.drawable.headbands
                    );

                    CartManager.addToCart(item);
                    Toast.makeText(product.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            });
        } else {
            Button buyNowBtn = bottomSheetView.findViewById(R.id.buyNowButton);
            buyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(quantityText.getText().toString());

                    int selectedRadioId = materialGroup.getCheckedRadioButtonId();
                    if (selectedRadioId == -1) {
                        Toast.makeText(product.this, "Please select a material", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RadioButton selectedRadio = bottomSheetView.findViewById(selectedRadioId);
                    String selectedMaterial = selectedRadio.getText().toString();

                    int unitPrice = 0;
                    if (selectedRadioId == R.id.customizeBtn) {
                        unitPrice = material1Price;
                    } else if (selectedRadioId == R.id.cusBtn) {
                        unitPrice = material2Price;
                    }

                    Intent intent = new Intent(product.this, Cart.class);
                    intent.putExtra("title", "Headbands");
                    intent.putExtra("price", String.valueOf(unitPrice));
                    intent.putExtra("quantity", qty);
                    intent.putExtra("material", selectedMaterial);
                    intent.putExtra("imageResId", R.drawable.headbands);

                    startActivity(intent);

                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
    }
}
