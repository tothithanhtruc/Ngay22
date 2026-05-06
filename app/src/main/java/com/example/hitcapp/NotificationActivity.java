package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rcvNotifications;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    private ImageView btnCartNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initViews();
        setupBottomNavigation();
        fetchNotifications();
    }

    private void initViews() {
        rcvNotifications = findViewById(R.id.rcvNotifications);
        btnCartNotify = findViewById(R.id.btnCartNotify);
        
        rcvNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList);
        rcvNotifications.setAdapter(adapter);

        // Xử lý click vào giỏ hàng
        if (btnCartNotify != null) {
            btnCartNotify.setOnClickListener(v -> {
                startActivity(new Intent(NotificationActivity.this, CartActivity.class));
            });
        }
    }

    private void fetchNotifications() {
        RetrofitClient.INSTANCE.getInstance().apiGetNotifications().enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notificationList.clear();
                    notificationList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    loadMockData();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                loadMockData();
                Toast.makeText(NotificationActivity.this, "Đang hiển thị dữ liệu mẫu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMockData() {
        notificationList.clear();
        notificationList.add(new Notification(
                "Giảm giá 30% cho ngày 8/3",
                "Nhập mã FLOWER83 để nhận ưu đãi ngay hôm nay.",
                "promo",
                "1", 
                "", 
                "08:00 - 08/03/2024",
                "Chào mừng ngày Quốc tế Phụ nữ 8/3! Shop hoa HITC dành tặng ưu đãi đặc biệt giảm ngay 30% cho tất cả các đơn đặt hàng trước. Nhập mã FLOWER83 tại màn hình thanh toán. Chúc các bạn một ngày thật ý nghĩa!"
        ));
        notificationList.add(new Notification(
                "Giao hàng thành công",
                "Đơn hàng #ORD123 đã được giao đến bạn.",
                "order",
                "123",
                "",
                "15:30 - 20/05/2024",
                "Đơn hàng mã số #ORD123 gồm 01 Bó hoa hồng đỏ đã được giao thành công đến địa chỉ của bạn. Hy vọng bạn hài lòng với dịch vụ của chúng tôi. Hãy để lại đánh giá để chúng tôi phục vụ tốt hơn nhé!"
        ));
        notificationList.add(new Notification(
                "Mẹo giữ hoa tươi lâu",
                "Khám phá những bí quyết giúp hoa rực rỡ suốt tuần...",
                "news",
                "0",
                "",
                "09:00 - 22/05/2024",
                "Bạn có biết: Thay nước mỗi ngày và cắt gốc hoa theo góc 45 độ sẽ giúp hoa hút nước tốt hơn? Đừng quên thêm một chút đường vào nước cắm hoa để cung cấp dinh dưỡng nhé. Xem thêm các mẹo khác tại blog của chúng tôi!"
        ));
        adapter.notifyDataSetChanged();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    return true;
                } else if (id == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}
