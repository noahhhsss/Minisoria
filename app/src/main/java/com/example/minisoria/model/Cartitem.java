package com.example.minisoria.model;

public class Cartitem {
    private String username;
    private String title;
    private String price;
    private boolean ischecked;
    private int quantity;
    private String material;
    private int imageResId;

    public Cartitem(String username, String title, String price, int quantity, String material, int imageResId) {
        this.username = username;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.material = material;
        this.imageResId = imageResId;

    }

    public String getUsername() { return username; }

    public boolean isChecked() {
        return ischecked;
    }
    public void setChecked(boolean checked) {
        this.ischecked = checked;
    }

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
}
