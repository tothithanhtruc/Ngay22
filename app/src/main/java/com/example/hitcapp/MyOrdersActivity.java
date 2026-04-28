package com.example.hitcapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MyOrdersActivity extends AppCompatActivity {

    private TextView lastSelectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Ánh xạ các tab
        TextView tabWaitConfirm = findViewById(R.id.tabWaitConfirm);
        TextView tabWaitPick = findViewById(R.id.tabWaitPick);
        TextView tabDelivering = findViewById(R.id.tabDelivering);
        TextView tabDelivered = findViewById(R.id.tabDelivered);
        TextView tabCancelled = findViewById(R.id.tabCancelled);
        TextView tabReturned = findViewById(R.id.tabReturned);

        lastSelectedTab = tabWaitConfirm; // Mặc định chọn tab đầu tiên

        // Gán sự kiện click cho các tab
        setupTabClick(tabWaitConfirm);
        setupTabClick(tabWaitPick);
        setupTabClick(tabDelivering);
        setupTabClick(tabDelivered);
        setupTabClick(tabCancelled);
        setupTabClick(tabReturned);
    }

    private void setupTabClick(TextView tab) {
        tab.setOnClickListener(v -> {
            // Reset tab cũ
            if (lastSelectedTab != null) {
                lastSelectedTab.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
                lastSelectedTab.setTypeface(null, android.graphics.Typeface.NORMAL);
            }

            // Highlight tab mới
            tab.setTextColor(ContextCompat.getColor(this, R.color.pink_primary));
            tab.setTypeface(null, android.graphics.Typeface.BOLD);
            lastSelectedTab = tab;

            // Xử lý lọc dữ liệu đơn hàng tương ứng (Giả lập)
            Toast.makeText(this, "Đang xem: " + tab.getText(), Toast.LENGTH_SHORT).show();
        });
    }
}
