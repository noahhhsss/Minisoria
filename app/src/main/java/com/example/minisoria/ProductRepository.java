package com.example.minisoria;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.minisoria.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final String PREF_NAME = "product_pref";
    private static final String PRODUCT_LIST_KEY = "product_list";
    private static List<Product> productList = new ArrayList<>();

    public static void initialize(Context context) {
        loadProducts(context);
    }

    public static List<Product> getProducts() {
        return productList;
    }

    public static void addProduct(Context context, Product product) {
        productList.add(product);
        saveProducts(context);
    }

    public static void removeProduct(Context context, Product product) {
        productList.remove(product);
        saveProducts(context);
    }

    public static void saveProducts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(productList);
        editor.putString(PRODUCT_LIST_KEY, json);
        editor.apply();
    }

    public static void loadProducts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(PRODUCT_LIST_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Product>>() {}.getType();
            productList = gson.fromJson(json, type);
        } else {
            productList = new ArrayList<>();
        }
    }
}
