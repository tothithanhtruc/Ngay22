package com.example.hitcapp;

public class CartItem {
    private String name;
    private String price;
    private int imageId;
    private int quantity;

    public CartItem(String name, String price, int imageId, int quantity) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageId() { return imageId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
