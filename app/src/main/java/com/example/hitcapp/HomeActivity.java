package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Nút Đăng xuất
        Button btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // 🛒 Nút Giỏ hàng (Mở CartActivity)
        ImageView btnCart = findViewById(R.id.btnCart);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        // 💬 Nút Tin nhắn
        ImageView btnMessage = findViewById(R.id.btnMessage);
        if (btnMessage != null) {
            btnMessage.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent);
            });
        }

        // 🌸 Sản phẩm 1 (Hoa Hồng)
        CardView cvProduct1 = findViewById(R.id.cvProduct1);
        if (cvProduct1 != null) {
            cvProduct1.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Hoa Hồng");
                intent.putExtra("productPrice", "199.000đ");
                intent.putExtra("productImage", R.drawable.hinh);
                startActivity(intent);
            });
        }

        // 🌸 Sản phẩm 2 (Hoa Tuylip)
        CardView cvProduct2 = findViewById(R.id.cvProduct2);
        if (cvProduct2 != null) {
            cvProduct2.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Hoa Tuylip");
                intent.putExtra("productPrice", "250.000đ");
                intent.putExtra("productImage", R.drawable.hinh2);
                startActivity(intent);
            });
        }

        // Thanh điều hướng bên dưới (Bottom Navigation)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_product) {
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    overridePendingTransition(0, 0);
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
}
