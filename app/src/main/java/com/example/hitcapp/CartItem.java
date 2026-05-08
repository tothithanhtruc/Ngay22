package com.example.hitcapp;

public class CartItem {
    private String name;
    private String price;
    private String imageUrl;
    private int quantity;
    private boolean isSelected;

    public CartItem(String name, String price, String imageUrl, int quantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.isSelected = false;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
