package com.example.hitcapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
    }
}
