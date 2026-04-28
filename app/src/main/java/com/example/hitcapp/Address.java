package com.example.hitcapp;

public class Address {
    private String name;
    private String phone;
    private String detail;
    private boolean isDefault;

    public Address(String name, String phone, String detail, boolean isDefault) {
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.isDefault = isDefault;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDetail() { return detail; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    @Override
    public String toString() {
        return name + " | " + phone + "\n" + detail;
    }
}
