package com.example.minisoria;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.ProductAdapter;
import com.example.minisoria.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AdminAddCaricatureActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProduct;
    private EditText editTextProductName, editTextProductPrice, editTextProductDescription;
    private EditText editTextMaterial1, editTextMaterial2, editTextMaterial3;
    private EditText editTextMaterialPrice1, editTextMaterialPrice2, editTextMaterialPrice3;
    private Uri selectedImageUri;
    private final String fixedCategory = "Caricature";
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminaddproduct); // Use the same layout

        imageViewProduct = findViewById(R.id.imageViewProduct);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextMaterial1 = findViewById(R.id.editTextMaterial1);
        editTextMaterial2 = findViewById(R.id.editTextMaterial2);
        editTextMaterial3 = findViewById(R.id.editTextMaterial3);
        editTextMaterialPrice1 = findViewById(R.id.editTextMaterialPrice1);
        editTextMaterialPrice2 = findViewById(R.id.editTextMaterialPrice2);
        editTextMaterialPrice3 = findViewById(R.id.editTextMaterialPrice3);
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);

        imageViewProduct.setOnClickListener(v -> openImageChooser());
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        ProductRepository.loadProducts(this);
        List<Product> productList = ProductRepository.getProducts();

        productAdapter = new ProductAdapter(this, productList, true,
                (product, position) -> {
                    // Delete click listener - remove product via repository
                    ProductRepository.removeProduct(this, product);
                    productAdapter.notifyItemRemoved(position);
                    Toast.makeText(this, product.getName() + " deleted", Toast.LENGTH_SHORT).show();
                },
                product -> {
                    // Item click listener
                    Intent intent = new Intent(this, ProductDetail.class);
                    intent.putExtra("product", product); // Product must implement Parcelable
                    startActivity(intent);
                });
        recyclerViewProducts.setAdapter(productAdapter);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        buttonAddProduct.setOnClickListener(v -> {
            String name = editTextProductName.getText().toString().trim();
            String priceStr = editTextProductPrice.getText().toString().trim();
            String description = editTextProductDescription.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Product name is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            if (priceStr.isEmpty()) {
                Toast.makeText(this, "Product price is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            double productPriceValue;
            try {
                productPriceValue = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid product price format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (description.isEmpty()) {
                Toast.makeText(this, "Product description is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select a product image", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> materials = new ArrayList<>();
            ArrayList<Double> prices = new ArrayList<>();
            addMaterialAndPrice(materials, prices, editTextMaterial1, editTextMaterialPrice1);
            addMaterialAndPrice(materials, prices, editTextMaterial2, editTextMaterialPrice2);
            addMaterialAndPrice(materials, prices, editTextMaterial3, editTextMaterialPrice3);

            if (materials.isEmpty()) {
                Toast.makeText(this, "Please enter at least one material with price", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(
                    0,
                    selectedImageUri.toString(),
                    name,
                    productPriceValue,
                    description,
                    materials,
                    prices,
                    fixedCategory // Always "Caricature"
            );

            // Load existing products, add the new one, and save
            ProductRepository.loadProducts(this);
            ProductRepository.addProduct(this, product); // ✅ This persists the new product

            Toast.makeText(this, "Caricature added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close and return to previous screen
        });
    }

    private void addMaterialAndPrice(ArrayList<String> materials, ArrayList<Double> prices,
                                     EditText materialEt, EditText priceEt) {
        String material = materialEt.getText().toString().trim();
        String priceStr = priceEt.getText().toString().trim();
        if (!material.isEmpty() && !priceStr.isEmpty()) {
            try {
                double price = Double.parseDouble(priceStr);
                materials.add(material);
                prices.add(price);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price input: " + priceStr, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewProduct.setImageURI(selectedImageUri);
            getContentResolver().takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

    }
    @Override
    public void onResume() {
        super.onResume();

        // Reload products and update the list
        ProductRepository.loadProducts(this);
        List<Product> allProducts = ProductRepository.getProducts();

        List<Product> caricatureList = new ArrayList<>();
        for (Product p : allProducts) {
            if ("Caricature".equalsIgnoreCase(p.getCategory())) {
                caricatureList.add(p);
            }
        }

        if (productAdapter != null) {
            productAdapter.updateProducts(caricatureList); // See fix 2 below
        }
    }



}
