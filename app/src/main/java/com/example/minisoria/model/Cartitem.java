package com.example.minisoria.model;

public class Cartitem {
    private String title;
    private String price;
    private String totalPrice;
    private int quantity;
    private int imageResId;

    public Cartitem(String title, String price, String totalPrice, int quantity, int imageResId) {
        this.title = title;
        this.price = price;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getTotalPrice() { return totalPrice; }
    public int getQuantity() { return quantity; }
    public int getImageResId() { return imageResId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
