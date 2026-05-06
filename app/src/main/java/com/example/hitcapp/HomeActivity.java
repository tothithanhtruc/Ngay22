package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvFeaturedProducts, rvPromoProducts;
    private ProductAdapter productAdapter, promoAdapter;
    private List<Product> productList = new ArrayList<>();
    private ImageView btnCart, btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvPromoProducts = findViewById(R.id.rvPromoProducts);
        btnCart = findViewById(R.id.btnCart);
        btnMessage = findViewById(R.id.btnMessage);

        rvFeaturedProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvPromoProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Sự kiện click giỏ hàng
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });

        // Sự kiện click tin nhắn
        btnMessage.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MessageActivity.class));
        });

        loadProductsFromApi();
        setupBottomNavigation();
    }

    private void loadProductsFromApi() {
        RetrofitClient.INSTANCE.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    displayData(productList);
                } else {
                    Toast.makeText(HomeActivity.this, "Lỗi tải sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_HOME", "Lỗi: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayData(List<Product> list) {
        if (list != null && !list.isEmpty()) {
            promoAdapter = new ProductAdapter(this, new ArrayList<>(list.subList(0, Math.min(list.size(), 5))));
            rvPromoProducts.setAdapter(promoAdapter);

            productAdapter = new ProductAdapter(this, list);
            rvFeaturedProducts.setAdapter(productAdapter);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_product) startActivity(new Intent(this, ProductActivity.class));
            else if (id == R.id.navigation_notifications) startActivity(new Intent(this, NotificationActivity.class));
            else if (id == R.id.navigation_profile) startActivity(new Intent(this, ProfileActivity.class));
            return true;
        });
    }
}
