package com.example.hitcapp;

import java.util.ArrayList;
import java.util.List;

public class AddressManager {
    private static List<Address> addressList = new ArrayList<>();

    static {
        // Địa chỉ mặc định ban đầu
        addressList.add(new Address("Nguyễn Văn A", "0123456789", "123 Đường ABC, Quận 1, TP. Hồ Chí Minh", true));
        addressList.add(new Address("Trần Thị B", "0987654321", "456 Đường XYZ, Quận 7, TP. Hồ Chí Minh", false));
    }

    public static List<Address> getAddressList() {
        return addressList;
    }

    public static void addAddress(Address address) {
        if (address.isDefault()) {
            for (Address a : addressList) a.setDefault(false);
        }
        addressList.add(0, address);
    }

    public static Address getDefaultAddress() {
        for (Address a : addressList) {
            if (a.isDefault()) return a;
        }
        return addressList.isEmpty() ? null : addressList.get(0);
    }
}
