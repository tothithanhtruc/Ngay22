package com.example.hitcapp;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static List<Order> orderList = new ArrayList<>();

    public static void addOrder(Order order) {
        orderList.add(0, order);
    }

    public static List<Order> getOrdersByStatus(String status) {
        List<Order> filtered = new ArrayList<>();
        for (Order o : orderList) {
            if (o.getStatus().equalsIgnoreCase(status)) {
                filtered.add(o);
            }
        }
        return filtered;
    }

    public static List<Order> getAllOrders() {
        return orderList;
    }
}
