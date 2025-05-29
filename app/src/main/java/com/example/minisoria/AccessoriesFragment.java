package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.ProductAdapter;
import com.example.minisoria.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AccessoriesFragment extends Fragment {

    private RecyclerView recyclerViewAccessories;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accessories, container, false);

        recyclerViewAccessories = view.findViewById(R.id.recyclerViewAccessories);

        // Setup RecyclerView with Grid layout, 2 columns
        int spanCount = 2;
        recyclerViewAccessories.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        // Load and filter accessories products
        List<Product> accessoriesList = getAccessoriesProducts();

        // Initialize adapter using your constructor for user mode with click listener
        productAdapter = new ProductAdapter(getContext(), accessoriesList, product -> {
            Intent intent = new Intent(getContext(), ProductDetail.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        recyclerViewAccessories.setAdapter(productAdapter);

        return view;
    }

    // Helper method to load products and filter by category
    private List<Product> getAccessoriesProducts() {
        ProductRepository.loadProducts(requireContext());
        List<Product> allProducts = ProductRepository.getProducts();
        List<Product> accessoriesList = new ArrayList<>();
        for (Product product : allProducts) {
            if ("Accessories".equalsIgnoreCase(product.getCategory())) {
                accessoriesList.add(product);
            }
        }
        return accessoriesList;
    }

    // Refresh the product list when fragment resumes to show newly added products
    @Override
    public void onResume() {
        super.onResume();
        if (productAdapter != null) {
            List<Product> updatedList = getAccessoriesProducts();
            productAdapter.updateList(updatedList);
        }
    }
}
