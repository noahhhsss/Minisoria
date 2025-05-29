package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.ProductAdapter;
import com.example.minisoria.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {

    private RecyclerView recyclerViewfood;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        recyclerViewfood = view.findViewById(R.id.recyclerViewCaricature); // Make sure this ID exists in your layout

        int spanCount = 2; // For grid layout

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerViewfood.setLayoutManager(layoutManager);

        // Load products from shared preferences / repository
        ProductRepository.loadProducts(requireContext());

        // Set up adapter
        List<Product> allProducts = ProductRepository.getProducts();

// Filter only caricature products
        List<Product> foodList = new ArrayList<>();
        for (Product p : allProducts) {
            if ("Food".equalsIgnoreCase(p.getCategory())) {
                foodList.add(p);
            }
        }

// Use filtered list in adapter
        productAdapter = new ProductAdapter(getContext(), foodList, false,
                null,
                product -> {
                    Intent intent = new Intent(getContext(), ProductDetail.class);
                    intent.putExtra("product", product);
                    startActivity(intent);
                });


        recyclerViewfood.setAdapter(productAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged();
        }
    }
}
