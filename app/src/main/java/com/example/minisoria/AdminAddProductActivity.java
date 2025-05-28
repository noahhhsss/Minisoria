package com.example.minisoria;

import android.annotation.SuppressLint;
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
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.Product;

import java.util.List;

public class AdminAddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewProduct;
    private EditText editTextProductName, editTextProductPrice;
    private EditText editTextMaterial1, editTextMaterial2, editTextMaterial3, editTextProductDescription;
    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;

    private RecyclerView recyclerViewProducts;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminaddproduct);

        imageViewProduct = findViewById(R.id.imageViewProduct);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextMaterial1 = findViewById(R.id.editTextMaterial1);
        editTextMaterial2 = findViewById(R.id.editTextMaterial2);
        editTextMaterial3 = findViewById(R.id.editTextMaterial3);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);

        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        imageViewProduct.setOnClickListener(v -> openImageChooser());

        buttonAddProduct.setOnClickListener(v -> {
            addProduct();
            loadProducts();
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadProducts() {
        productList = databaseHelper.getAllProducts();
        adapter = new ProductAdapter(this, productList, true,
                (product, position) -> {
                    // Delete product logic
                    boolean deleted = databaseHelper.deleteProduct(product.getId());
                    if (deleted) {
                        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                        adapter.removeItem(position);
                    } else {
                        Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                },
                product -> {
                    // Open ProductDetail activity on click with correct keys
                    Intent intent = new Intent(AdminAddProductActivity.this, ProductDetail.class);
                    intent.putExtra("product_name", product.getName());
                    intent.putExtra("product_price", product.getPrice()); // if double/float, convert to string
                    intent.putExtra("product_description", product.getDescription());
                    intent.putExtra("product_image_uri", product.getImageUri());
                    startActivity(intent);
                }
        );

        recyclerViewProducts.setAdapter(adapter);
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
            Uri imageUri = data.getData();
            selectedImageUri = imageUri;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                @SuppressLint("NewApi")
                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } else {
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            imageViewProduct.setImageURI(imageUri);
        }
    }

    private void addProduct() {
        String name = editTextProductName.getText().toString().trim();
        String price = editTextProductPrice.getText().toString().trim();
        String description = editTextProductDescription.getText().toString().trim();

        StringBuilder materialBuilder = new StringBuilder();
        if (!editTextProductDescription.getText().toString().trim().isEmpty()) {
            materialBuilder.append(editTextProductDescription.getText().toString().trim());
        }
        String material = materialBuilder.toString();

        if (name.isEmpty() || price.isEmpty() || material.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = databaseHelper.insertProduct(name, price, material, description, selectedImageUri.toString());

        if (success) {
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
            selectedImageUri = null;
            imageViewProduct.setImageResource(R.drawable.product); // reset to placeholder
            editTextProductName.setText("");
            editTextProductPrice.setText("");
            editTextProductDescription.setText("");
            editTextMaterial1.setText("");
            editTextMaterial2.setText("");
            editTextMaterial3.setText("");
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }
}
