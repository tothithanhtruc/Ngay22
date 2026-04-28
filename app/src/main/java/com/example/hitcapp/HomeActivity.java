package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<String> searchHistory = new ArrayList<>();
    private ArrayAdapter<String> historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Ánh xạ các view
        CardView cvProduct1 = findViewById(R.id.cvProduct1);
        CardView cvProduct2 = findViewById(R.id.cvProduct2);
        TextView label1 = findViewById(R.id.label1);
        TextView tvNoResults = findViewById(R.id.tvNoResults);

        // 🔍 Xử lý thanh tìm kiếm với Lịch sử (AutoCompleteTextView)
        AutoCompleteTextView searchBar = findViewById(R.id.searchBar);
        
        // Giả lập một số lịch sử tìm kiếm ban đầu
        searchHistory.add("Hoa hồng");
        searchHistory.add("Hoa tuylip");
        searchHistory.add("Quà tặng");

        historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchHistory);
        if (searchBar != null) {
            searchBar.setAdapter(historyAdapter);

            searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String query = s.toString().toLowerCase().trim();

                    if (query.isEmpty()) {
                        cvProduct1.setVisibility(View.VISIBLE);
                        cvProduct2.setVisibility(View.VISIBLE);
                        label1.setVisibility(View.VISIBLE);
                        tvNoResults.setVisibility(View.GONE);
                        return;
                    }

                    // Logic lọc
                    boolean match1 = "hoa hồng".contains(query);
                    boolean match2 = "hoa tuylip".contains(query);

                    cvProduct1.setVisibility(match1 ? View.VISIBLE : View.GONE);
                    cvProduct2.setVisibility(match2 ? View.VISIBLE : View.GONE);

                    // Hiển thị thông báo nếu không tìm thấy
                    if (!match1 && !match2) {
                        tvNoResults.setVisibility(View.VISIBLE);
                        label1.setVisibility(View.GONE);
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                        label1.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Khi người dùng nhấn Enter hoặc chọn từ lịch sử
            searchBar.setOnItemClickListener((parent, view, position, id) -> {
                String selection = (String) parent.getItemAtPosition(position);
                addToHistory(selection);
            });

            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                String text = searchBar.getText().toString().trim();
                if (!text.isEmpty()) {
                    addToHistory(text);
                }
                return false;
            });
        }

        // 🛒 Giỏ hàng
        ImageView btnCart = findViewById(R.id.btnCart);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        // 💬 Tin nhắn
        ImageView btnMessage = findViewById(R.id.btnMessage);
        if (btnMessage != null) {
            btnMessage.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent);
            });
        }

        // 🌸 Chi tiết Sản phẩm
        if (cvProduct1 != null) {
            cvProduct1.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Hoa Hồng");
                intent.putExtra("productPrice", "199.000đ");
                intent.putExtra("productImage", R.drawable.hinh);
                startActivity(intent);
            });
        }
        if (cvProduct2 != null) {
            cvProduct2.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Hoa Tuylip");
                intent.putExtra("productPrice", "250.000đ");
                intent.putExtra("productImage", R.drawable.hinh2);
                startActivity(intent);
            });
        }

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) return true;
                if (id == R.id.navigation_product) {
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    return true;
                }
                if (id == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    return true;
                }
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
                }
                return false;
            });
        }
    }

    private void addToHistory(String query) {
        if (!searchHistory.contains(query)) {
            searchHistory.add(0, query);
            historyAdapter.notifyDataSetChanged();
        }
    }
}
