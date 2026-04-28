package com.example.hitcapp;

public class Product {
    private String name;
    private String price;
    private int imageId;

    public Product(String name, String price, int imageId) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageId() { return imageId; }
}
