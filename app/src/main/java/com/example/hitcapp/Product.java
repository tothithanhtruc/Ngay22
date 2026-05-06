package com.example.hitcapp;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("price")
    private double price;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("image")
    private String image;

    public Product(int id, String title, double price, String description, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
