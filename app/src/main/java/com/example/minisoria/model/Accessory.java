package com.example.minisoria.model;

import android.net.Uri;

public class Accessory {
    private int imageResId = -1;
    private Uri imageUri = null;
    private String name;
    private double price;
    private String description; // ✅ Add this

    // Constructor for drawable resource image
    public Accessory(int imageResId, String name, double price, String description) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.description = description; // ✅ Set description
    }

    // Constructor for image Uri (from gallery)
    public Accessory(Uri imageUri, String name, double price, String description) {
        this.imageUri = imageUri;
        this.name = name;
        this.price = price;
        this.description = description; // ✅ Set description
    }

    public int getImageResId() {
        return imageResId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() { // ✅ Getter method
        return description;
    }

    public boolean hasImageUri() {
        return imageUri != null;
    }
}
