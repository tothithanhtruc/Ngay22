package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private EditText etSearch;
    private ImageView btnCartProduct;
    private List<Product> allProductsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        rvProducts = findViewById(R.id.rvProducts);
        etSearch = findViewById(R.id.etSearch);
        btnCartProduct = findViewById(R.id.btnCartProduct);
        
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Xử lý click vào giỏ hàng
        if (btnCartProduct != null) {
            btnCartProduct.setOnClickListener(v -> {
                startActivity(new Intent(ProductActivity.this, CartActivity.class));
            });
        }

        loadProductsFromApi();
        setupSearch();
        setupBottomNavigation();
    }

    private void loadProductsFromApi() {
        RetrofitClient.INSTANCE.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProductsList = response.body();
                    productAdapter = new ProductAdapter(ProductActivity.this, allProductsList);
                    rvProducts.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductActivity.this, "Lỗi API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(ProductActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { filter(s.toString()); }
        });
    }

    private void filter(String text) {
        List<Product> filtered = new ArrayList<>();
        for (Product item : allProductsList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (productAdapter != null) productAdapter.setFilteredList(filtered);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_product);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            }
            if (id == R.id.navigation_notifications) {
                startActivity(new Intent(this, NotificationActivity.class));
                return true;
            }
            if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return id == R.id.navigation_product;
        });
    }
}
