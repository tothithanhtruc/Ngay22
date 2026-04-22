package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnOrder = findViewById(R.id.btnOrder);

        btnBack.setOnClickListener(v -> finish());

        btnOrder.setOnClickListener(v -> {
            // Chuyển sang màn hình Đặt hàng thành công
            Intent intent = new Intent(PaymentActivity.this, OrderSuccessActivity.class);
            startActivity(intent);
            // Kết thúc PaymentActivity để người dùng không quay lại được trang thanh toán bằng nút back
            finish();
        });
    }
}
