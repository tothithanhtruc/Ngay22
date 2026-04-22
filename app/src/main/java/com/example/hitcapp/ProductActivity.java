package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Ánh xạ và xử lý click cho từng sản phẩm
        setupProductClick(R.id.cardProduct1, "Hoa Hồng Đỏ", "200.000đ", R.drawable.hinh6, R.id.btnGoToCart1);
        setupProductClick(R.id.cardProduct2, "Hoa Tulip Vàng", "150.000đ", R.drawable.hinh2, R.id.btnGoToCart2);
        setupProductClick(R.id.cardProduct3, "Hoa Cúc Trắng", "120.000đ", R.drawable.hinh, R.id.btnGoToCart3);
        setupProductClick(R.id.cardProduct4, "Hoa Lan Hồ Điệp", "450.000đ", R.drawable.hinh3, R.id.btnGoToCart4);
        setupProductClick(R.id.cardProduct5, "Hoa Hướng Dương", "180.000đ", R.drawable.hinh4, R.id.btnGoToCart5);
        setupProductClick(R.id.cardProduct6, "Hoa Ly Trắng", "220.000đ", R.drawable.hinh5, R.id.btnGoToCart6);

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_product);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.navigation_product) {
                    return true;
                } else if (id == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            });
        }
    }

    private void setupProductClick(int cardId, String name, String price, int imageRes, int cartBtnId) {
        CardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", name);
                intent.putExtra("productPrice", price);
                intent.putExtra("productImage", imageRes);
                startActivity(intent);
            });
        }

        ImageView btnGoToCart = findViewById(cartBtnId);
        if (btnGoToCart != null) {
            btnGoToCart.setOnClickListener(v -> {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }
    }
}
