package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView rvProducts, rvCategories;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private EditText etSearch;
    private ImageView btnCartProduct;
    private List<Product> allProductsList = new ArrayList<>();
    private List<String> categoriesList = new ArrayList<>();
    private String currentCategory = "Tất cả";
    
    // Bản đồ dịch danh mục
    private static final Map<String, String> categoryTranslation = new HashMap<>();
    static {
        categoryTranslation.put("electronics", "Điện tử");
        categoryTranslation.put("jewelery", "Trang sức");
        categoryTranslation.put("men's clothing", "Thời trang nam");
        categoryTranslation.put("women's clothing", "Thời trang nữ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        rvProducts = findViewById(R.id.rvProducts);
        rvCategories = findViewById(R.id.rvCategories);
        etSearch = findViewById(R.id.etSearch);
        btnCartProduct = findViewById(R.id.btnCartProduct);
        
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (btnCartProduct != null) {
            btnCartProduct.setOnClickListener(v -> {
                startActivity(new Intent(ProductActivity.this, CartActivity.class));
            });
        }

        loadProductsFromApi();
        loadCategoriesFromApi();
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
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void loadCategoriesFromApi() {
        RetrofitClient.INSTANCE.getInstance().getCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesList.clear();
                    categoriesList.add("Tất cả");
                    
                    // Thêm các danh mục đã được dịch
                    for (String cat : response.body()) {
                        String translated = categoryTranslation.get(cat.toLowerCase());
                        categoriesList.add(translated != null ? translated : cat);
                    }
                    
                    categoryAdapter = new CategoryAdapter(categoriesList, category -> {
                        currentCategory = category;
                        filter(etSearch.getText().toString());
                    });
                    rvCategories.setAdapter(categoryAdapter);
                    rvCategories.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi category: " + t.getMessage());
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
            boolean matchesSearch = item.getTitle().toLowerCase().contains(text.toLowerCase());
            
            // Tìm tên tiếng Anh tương ứng với tên tiếng Việt đang chọn
            String englishCategoryOfItem = item.getCategory();
            String translatedCategoryOfItem = categoryTranslation.get(englishCategoryOfItem.toLowerCase());
            if (translatedCategoryOfItem == null) translatedCategoryOfItem = englishCategoryOfItem;

            boolean matchesCategory = currentCategory.equals("Tất cả") || translatedCategoryOfItem.equals(currentCategory);
            
            if (matchesSearch && matchesCategory) {
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
            if (id == R.id.navigation_product) {
                return true;
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.navigation_notifications) {
                startActivity(new Intent(this, NotificationActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
