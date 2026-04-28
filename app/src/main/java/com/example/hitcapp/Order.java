package com.example.hitcapp;

import java.util.List;

public class Order {
    private String orderId;
    private List<CartItem> items;
    private long totalAmount;
    private String status; // "WaitConfirm", "WaitPick", "Delivering", "Delivered", "Cancelled", "Returned"
    private String date;

    public Order(String orderId, List<CartItem> items, long totalAmount, String status, String date) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.date = date;
    }

    public String getOrderId() { return orderId; }
    public List<CartItem> getItems() { return items; }
    public long getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
}
