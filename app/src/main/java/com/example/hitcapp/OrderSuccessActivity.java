package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);
        
        btnContinueShopping.setOnClickListener(v -> {
            // Quay về màn hình Home và xóa hết các màn hình cũ
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
