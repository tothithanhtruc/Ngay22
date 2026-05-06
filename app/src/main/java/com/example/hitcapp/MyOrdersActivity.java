package com.example.hitcapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    private TextView lastSelectedTab;
    private RecyclerView rcvOrders, rcvRecommended;
    private OrderAdapter orderAdapter;
    private List<Order> displayedOrders = new ArrayList<>();
    private LinearLayout orderEmptyPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        initViews();
        setupTabs();
        loadRecommendations();
        
        // Mặc định hiển thị tab Chờ xác nhận đầu tiên
        filterOrders("WaitConfirm");
    }

    private void initViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        rcvOrders = findViewById(R.id.rcvOrders);
        rcvRecommended = findViewById(R.id.rcvRecommended);
        orderEmptyPlaceholder = findViewById(R.id.orderEmptyPlaceholder);

        // Setup RecyclerView đơn hàng
        rcvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this, displayedOrders);
        rcvOrders.setAdapter(orderAdapter);
    }

    private void setupTabs() {
        TextView tabWaitConfirm = findViewById(R.id.tabWaitConfirm);
        TextView tabWaitPick = findViewById(R.id.tabWaitPick);
        TextView tabDelivering = findViewById(R.id.tabDelivering);
        TextView tabDelivered = findViewById(R.id.tabDelivered);
        TextView tabCancelled = findViewById(R.id.tabCancelled);
        TextView tabReturned = findViewById(R.id.tabReturned);

        lastSelectedTab = tabWaitConfirm;

        setupTabClick(tabWaitConfirm, "WaitConfirm");
        setupTabClick(tabWaitPick, "WaitPick");
        setupTabClick(tabDelivering, "Delivering");
        setupTabClick(tabDelivered, "Delivered");
        setupTabClick(tabCancelled, "Cancelled");
        setupTabClick(tabReturned, "Returned");
    }

    private void setupTabClick(TextView tab, String status) {
        tab.setOnClickListener(v -> {
            // UI Update
            if (lastSelectedTab != null) {
                lastSelectedTab.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                lastSelectedTab.setTypeface(null, android.graphics.Typeface.NORMAL);
            }
            tab.setTextColor(ContextCompat.getColor(this, R.color.pink_primary));
            tab.setTypeface(null, android.graphics.Typeface.BOLD);
            lastSelectedTab = tab;

            // Dữ liệu Update
            filterOrders(status);
        });
    }

    private void filterOrders(String status) {
        List<Order> filtered = OrderManager.getOrdersByStatus(status);
        displayedOrders.clear();
        displayedOrders.addAll(filtered);
        orderAdapter.notifyDataSetChanged();

        // Hiển thị thông báo nếu không có đơn hàng
        if (displayedOrders.isEmpty()) {
            orderEmptyPlaceholder.setVisibility(View.VISIBLE);
            rcvOrders.setVisibility(View.GONE);
        } else {
            orderEmptyPlaceholder.setVisibility(View.GONE);
            rcvOrders.setVisibility(View.VISIBLE);
        }
    }

    private void loadRecommendations() {
        // Load sản phẩm gợi ý từ API giống trang chi tiết
        RetrofitClient.INSTANCE.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    ProductAdapter adapter = new ProductAdapter(MyOrdersActivity.this, products.subList(0, Math.min(products.size(), 6)));
                    rcvRecommended.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rcvRecommended.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {}
        });
    }
}
