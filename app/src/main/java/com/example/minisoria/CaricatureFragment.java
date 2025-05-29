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

import java.util.List;

public class CaricatureFragment extends Fragment {

    private RecyclerView recyclerViewCaricatures;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caricature2, container, false);

        recyclerViewCaricatures = view.findViewById(R.id.recyclerViewCaricature); // Make sure this ID exists in your layout

        int spanCount = 2; // For grid layout

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerViewCaricatures.setLayoutManager(layoutManager);

        // Load products from shared preferences / repository
        ProductRepository.loadProducts(requireContext());
        List<Product> productList = ProductRepository.getProducts();

        // Set up adapter
        productAdapter = new ProductAdapter(getContext(), productList, false,
                null, // No delete listener
                product -> {
                    Intent intent = new Intent(getContext(), ProductDetail.class);
                    intent.putExtra("product", product); // Make sure Product implements Parcelable
                    startActivity(intent);
                });

        recyclerViewCaricatures.setAdapter(productAdapter);

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
