package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSuccessActivity extends AppCompatActivity {

    private RecyclerView rvSuggestions;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initViews();
        loadSuggestions();
    }

    private void initViews() {
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);
        Button btnViewOrder = findViewById(R.id.btnViewOrder); // Nút mới
        rvSuggestions = findViewById(R.id.rvSuggestions);
        
        // Hiển thị gợi ý dạng lưới 2 cột
        rvSuggestions.setLayoutManager(new GridLayoutManager(this, 2));

        // Xử lý nút Tiếp tục mua sắm
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Xử lý nút Xem đơn hàng
        if (btnViewOrder != null) {
            btnViewOrder.setOnClickListener(v -> {
                // Mở trang Đơn hàng của tôi
                Intent intent = new Intent(OrderSuccessActivity.this, MyOrdersActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void loadSuggestions() {
        RetrofitClient.INSTANCE.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    List<Product> limitedList = products.subList(0, Math.min(products.size(), 6));
                    
                    productAdapter = new ProductAdapter(OrderSuccessActivity.this, limitedList);
                    rvSuggestions.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "OrderSuccess Suggestions: " + t.getMessage());
            }
        });
    }
}
