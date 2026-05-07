package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);

        loadUserData();

        // 🛒 Nút Giỏ hàng trong trang Profile
        ImageView btnCart = findViewById(R.id.btnCartProfile);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        // ⚙️ Nút Cài đặt
        LinearLayout btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }

        // 📦 Nút Đơn hàng của tôi
        LinearLayout btnOrders = findViewById(R.id.btnOrders);
        if (btnOrders != null) {
            btnOrders.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            });
        }

        // 🔍 Xem lại sản phẩm đã mua (Chuyển đến tab Đã giao trong Đơn hàng)
        LinearLayout btnViewPurchased = findViewById(R.id.btnViewProductDetailFromProfile);
        if (btnViewPurchased != null) {
            btnViewPurchased.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MyOrdersActivity.class);
                intent.putExtra("ORDER_STATUS", "Delivered");
                startActivity(intent);
            });
        }

        // Nút Đăng xuất trong trang Profile
        LinearLayout btnLogout = findViewById(R.id.btnLogoutProfile);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
                // Quay lại màn hình Đăng nhập (MainActivity) và xóa lịch sử
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Thanh điều hướng bên dưới
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_product) {
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_profile) {
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        if (tvUserName != null) tvUserName.setText(UserManager.getUserName(this));
        if (tvUserEmail != null) tvUserEmail.setText(UserManager.getUserEmail(this));
    }
}
