package com.example.minisoria.model;

import android.net.Uri;

public class Cartitem {
    private String username;
    private String title;
    private String price;
    private boolean ischecked;
    private int quantity;
    private String material;
    private int imageResId;
    private Uri imageUri;

    // ✅ Constructor with imageUri
    public Cartitem(String username, String title, String price, int quantity, String material, int imageResId, Uri imageUri) {

        this.username = username;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.material = material;
        this.imageResId = imageResId;
        this.imageUri = imageUri;
    }

    // ✅ Optional: Keep old constructor for legacy use
    public Cartitem(String username, String title, String price, int quantity, String material, int imageResId) {
        this(username, title, price, quantity, material, imageResId, null);

    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public Uri getImageUri() { return imageUri; }
    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }

    public boolean isChecked() { return ischecked; }
    public void setChecked(boolean checked) { this.ischecked = checked; }
}
