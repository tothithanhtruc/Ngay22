package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rcvCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // Ánh xạ View
        rcvCart = findViewById(R.id.rcvCart);
        tvTitle = findViewById(R.id.tvTitle);
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnBuy = findViewById(R.id.btnBuy);

        // Khởi tạo danh sách sản phẩm (Dữ liệu mẫu)
        cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("Hoa Hồng", "199.000đ", R.drawable.hinh, 1));
        cartItemList.add(new CartItem("Hoa Tuylip", "250.000đ", R.drawable.hinh2, 2));

        // Thiết lập RecyclerView
        cartAdapter = new CartAdapter(cartItemList);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartAdapter);

        // Cập nhật số lượng trên tiêu đề
        updateCartCount();

        // Nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 💳 Nút Thanh toán
        if (btnBuy != null) {
            btnBuy.setOnClickListener(v -> {
                // Chuyển sang màn hình Thanh toán
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
            });
        }
    }

    private void updateCartCount() {
        if (tvTitle != null) {
            tvTitle.setText("Giỏ hàng (" + cartItemList.size() + ")");
        }
    }
}
