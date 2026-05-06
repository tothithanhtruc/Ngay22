package com.example.hitcapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        ImageView btnBack = findViewById(R.id.btnBackDetail);
        ImageView imgIcon = findViewById(R.id.imgDetailIcon);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvTime = findViewById(R.id.tvDetailTime);
        TextView tvContent = findViewById(R.id.tvDetailContent);
        Button btnAction = findViewById(R.id.btnAction);

        // Lấy đối tượng Notification từ Intent
        Notification notification = (Notification) getIntent().getSerializableExtra("notification");

        if (notification != null) {
            tvTitle.setText(notification.getTitle());
            tvTime.setText(notification.getTimestamp());
            tvContent.setText(notification.getFullMessage());

            // Thiết lập Icon và Action dựa trên loại thông báo
            String type = notification.getType();
            if ("promo".equals(type)) {
                imgIcon.setImageResource(android.R.drawable.ic_menu_send);
                imgIcon.setColorFilter(0xFFE91E63);
                btnAction.setText("NHẬN ƯU ĐÃI NGAY");
            } else if ("order".equals(type)) {
                imgIcon.setImageResource(android.R.drawable.ic_menu_agenda);
                imgIcon.setColorFilter(0xFF4CAF50);
                btnAction.setText("KIỂM TRA ĐƠN HÀNG");
            } else {
                imgIcon.setImageResource(android.R.drawable.ic_dialog_info);
                imgIcon.setColorFilter(0xFF2196F3);
                btnAction.setText("ĐÓNG");
            }
        }

        btnBack.setOnClickListener(v -> finish());
        
        btnAction.setOnClickListener(v -> {
            // Xử lý hành động khi nhấn nút (ví dụ: quay lại hoặc mở trang tương ứng)
            finish();
        });
    }
}