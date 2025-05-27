package com.example.minisoria.model;

public class Order {

    private int orderId;       // Unique order ID
    private int userId;        // User who placed the order
    private String userName;   // User's name
    private String title;      // Title of the order/product
    private String material;   // Material description
    private int quantity;      // Quantity ordered
    private double totalPrice; // Total price of the order
    private String status;     // Status of the order ("pending", "accepted", etc.)

    // Constructor with all fields
    public Order(int orderId, int userId, String userName, String title, String material, int quantity, double totalPrice, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.material = material;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and setters for each field

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
