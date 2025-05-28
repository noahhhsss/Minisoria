package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.minisoria.db.DatabaseHelper;
import com.example.minisoria.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AccessoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    public AccessoriesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accessories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewAccessories);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        dbHelper = new DatabaseHelper(getContext());

        productList = new ArrayList<>();

        // Set adapter once, assigning to the field, with item click listener
        adapter = new ProductAdapter(requireContext(), productList, product -> {
            // Start ProductDetail Activity on click
            Intent intent = new Intent(requireContext(), ProductDetail.class);
            intent.putExtra("product", product);  // make Product Serializable or Parcelable
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        loadProductsFromDb();
    }


    private void loadProductsFromDb() {
        List<Product> productsFromDb = dbHelper.getAllProducts();
        productList.clear();
        if (productsFromDb != null) {
            productList.addAll(productsFromDb);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProductsFromDb();
        adapter.notifyDataSetChanged();
    }

}
