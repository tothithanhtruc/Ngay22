package com.example.hitcapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    public static void addItem(CartItem item) {
        for (CartItem existingItem : cartItems) {
            if (existingItem.getName().equals(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void clearSelectedItems() {
        // Xóa các sản phẩm đang được tích chọn
        cartItems.removeIf(CartItem::isSelected);
    }

    public static int getCartSize() {
        return cartItems.size();
    }
}
