package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView rcvCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvTitle, tvTotal;
    private CheckBox cbSelectAll;
    private Button btnBuy;

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
        ImageView btnBack = findViewById(R.id.btnBack);

        // Lấy danh sách sản phẩm
        cartItemList = CartManager.getCartItems();

        // Thiết lập RecyclerView với Listener
        cartAdapter = new CartAdapter(cartItemList, this);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartAdapter);

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

        // 💳 Nút Thanh toán
        if (btnBuy != null) {
            btnBuy.setOnClickListener(v -> {
                // Kiểm tra xem có sản phẩm nào được chọn không
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
            });
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
                    e.printStackTrace();
                }
            }
        }

        // Hiển thị tổng tiền
        if (tvTotal != null) {
            tvTotal.setText(formatPrice(total));
        }

        // Cập nhật trạng thái nút "Chọn tất cả"
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
