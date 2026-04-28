package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ View
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView imgProductDetail = findViewById(R.id.imgProductDetail);
        TextView tvProductNameDetail = findViewById(R.id.tvProductNameDetail);
        TextView tvProductPriceDetail = findViewById(R.id.tvProductPriceDetail);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);
        RecyclerView rcvRecommended = findViewById(R.id.rcvRecommendedDetail);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("productName");
        String price = getIntent().getStringExtra("productPrice");
        int imageId = getIntent().getIntExtra("productImage", R.drawable.hinh);

        // Hiển thị dữ liệu
        if (name != null) tvProductNameDetail.setText(name);
        if (price != null) tvProductPriceDetail.setText(price);
        imgProductDetail.setImageResource(imageId);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // 🛒 Xử lý thêm vào giỏ hàng
        if (btnAddToCart != null) {
            btnAddToCart.setOnClickListener(v -> {
                CartItem newItem = new CartItem(name, price, imageId, 1);
                CartManager.addItem(newItem);
                Toast.makeText(this, "Đã thêm " + name + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            });
        }

        // ⚡ Xử lý Mua ngay
        if (btnBuyNow != null) {
            btnBuyNow.setOnClickListener(v -> {
                for (CartItem item : CartManager.getCartItems()) {
                    item.setSelected(false);
                }
                CartItem targetItem = null;
                for (CartItem item : CartManager.getCartItems()) {
                    if (item.getName().equals(name)) {
                        targetItem = item;
                        break;
                    }
                }
                if (targetItem == null) {
                    targetItem = new CartItem(name, price, imageId, 1);
                    CartManager.getCartItems().add(targetItem);
                }
                targetItem.setSelected(true);
                Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
                startActivity(intent);
            });
        }

        // 🌸 Hiển thị Sản phẩm bạn có thể thích
        setupRecommendations(rcvRecommended);
    }

    private void setupRecommendations(RecyclerView rcv) {
        if (rcv == null) return;
        List<Product> recommendedList = new ArrayList<>();
        recommendedList.add(new Product("Hoa Cúc Trắng", "120.000đ", R.drawable.hinh));
        recommendedList.add(new Product("Hoa Lan Hồ Điệp", "450.000đ", R.drawable.hinh3));
        recommendedList.add(new Product("Hoa Hướng Dương", "180.000đ", R.drawable.hinh4));
        recommendedList.add(new Product("Hoa Ly Trắng", "220.000đ", R.drawable.hinh5));

        ProductAdapter adapter = new ProductAdapter(this, recommendedList);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcv.setAdapter(adapter);
    }
}
