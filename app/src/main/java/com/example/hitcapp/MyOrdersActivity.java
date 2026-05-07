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
        
        // Kiểm tra xem có yêu cầu mở tab cụ thể không (mặc định là WaitConfirm)
        String initialStatus = getIntent().getStringExtra("ORDER_STATUS");
        if (initialStatus == null) {
            initialStatus = "WaitConfirm";
        }
        
        selectTabByStatus(initialStatus);
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

        setupTabClick(tabWaitConfirm, "WaitConfirm");
        setupTabClick(tabWaitPick, "WaitPick");
        setupTabClick(tabDelivering, "Delivering");
        setupTabClick(tabDelivered, "Delivered");
        setupTabClick(tabCancelled, "Cancelled");
        setupTabClick(tabReturned, "Returned");
    }

    private void setupTabClick(TextView tab, String status) {
        tab.setOnClickListener(v -> {
            updateTabUI(tab);
            filterOrders(status);
        });
    }

    private void selectTabByStatus(String status) {
        TextView targetTab;
        switch (status) {
            case "WaitPick": targetTab = findViewById(R.id.tabWaitPick); break;
            case "Delivering": targetTab = findViewById(R.id.tabDelivering); break;
            case "Delivered": targetTab = findViewById(R.id.tabDelivered); break;
            case "Cancelled": targetTab = findViewById(R.id.tabCancelled); break;
            case "Returned": targetTab = findViewById(R.id.tabReturned); break;
            default: targetTab = findViewById(R.id.tabWaitConfirm); break;
        }
        
        if (targetTab != null) {
            updateTabUI(targetTab);
            filterOrders(status);
        }
    }

    private void updateTabUI(TextView selectedTab) {
        if (lastSelectedTab != null) {
            lastSelectedTab.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            lastSelectedTab.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
        selectedTab.setTextColor(ContextCompat.getColor(this, R.color.pink_primary));
        selectedTab.setTypeface(null, android.graphics.Typeface.BOLD);
        lastSelectedTab = selectedTab;
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
