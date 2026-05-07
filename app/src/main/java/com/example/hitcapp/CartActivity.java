package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView rcvCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvTitle, tvTotal, btnEdit;
    private CheckBox cbSelectAll;
    private Button btnBuy;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // Ánh xạ View
        rcvCart = findViewById(R.id.rcvCart);
        tvTitle = findViewById(R.id.tvTitle);
        tvTotal = findViewById(R.id.tvTotal);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnBuy = findViewById(R.id.btnBuy);
        btnEdit = findViewById(R.id.btnEdit);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Lấy danh sách sản phẩm
        cartItemList = CartManager.getCartItems();

        // Thiết lập RecyclerView với Listener
        cartAdapter = new CartAdapter(cartItemList, this);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartAdapter);

        // Xử lý nút Sửa
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                isEditMode = !isEditMode;
                if (isEditMode) {
                    btnEdit.setText("Xong");
                    btnBuy.setText("Xóa");
                    btnBuy.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE91E63));
                } else {
                    btnEdit.setText("Sửa");
                    btnBuy.setText("Thanh toán");
                    btnBuy.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFF5F00));
                }
            });
        }

        // Xử lý nút Chọn tất cả
        if (cbSelectAll != null) {
            cbSelectAll.setOnClickListener(v -> {
                boolean isChecked = cbSelectAll.isChecked();
                for (CartItem item : cartItemList) {
                    item.setSelected(isChecked);
                }
                cartAdapter.notifyDataSetChanged();
                onAmountChanged();
            });
        }

        // Cập nhật giao diện ban đầu
        updateCartCount();
        onAmountChanged();

        // Nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 💳 Nút Thanh toán / Xóa
        if (btnBuy != null) {
            btnBuy.setOnClickListener(v -> {
                if (isEditMode) {
                    // Chế độ Xóa
                    deleteSelectedItems();
                } else {
                    // Chế độ Thanh toán
                    boolean hasSelection = false;
                    for (CartItem item : cartItemList) {
                        if (item.isSelected()) {
                            hasSelection = true;
                            break;
                        }
                    }

                    if (hasSelection) {
                        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void deleteSelectedItems() {
        Iterator<CartItem> iterator = cartItemList.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.isSelected()) {
                iterator.remove();
                removed = true;
            }
        }
        
        if (removed) {
            cartAdapter.notifyDataSetChanged();
            onAmountChanged();
            updateCartCount();
            Toast.makeText(this, "Đã xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vui lòng chọn sản phẩm cần xóa!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
            onAmountChanged();
        }
    }

    @Override
    public void onAmountChanged() {
        long total = 0;
        int selectedCount = 0;

        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                selectedCount++;
                try {
                    String priceStr = item.getPrice().replace(".", "").replace("đ", "").trim();
                    long priceValue = Long.parseLong(priceStr);
                    total += priceValue * item.getQuantity();
                } catch (Exception e) {
                    // Handle $ prices if any
                    try {
                        String clean = item.getPrice().replaceAll("[^0-9]", "");
                        total += Long.parseLong(clean) * item.getQuantity();
                    } catch (Exception ignored) {}
                }
            }
        }

        if (tvTotal != null) {
            tvTotal.setText(formatPrice(total));
        }

        if (cbSelectAll != null) {
            cbSelectAll.setChecked(selectedCount == cartItemList.size() && cartItemList.size() > 0);
        }
        
        updateCartCount();
    }

    private void updateCartCount() {
        if (tvTitle != null) {
            tvTitle.setText("Giỏ hàng (" + cartItemList.size() + ")");
        }
    }

    private String formatPrice(long price) {
        return String.format("%,d", price).replace(',', '.') + "đ";
    }
}
