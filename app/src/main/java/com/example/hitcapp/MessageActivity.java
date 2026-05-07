package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // RecyclerView setup
        RecyclerView rcvMessages = findViewById(R.id.rcvMessages);
        if (rcvMessages != null) {
            List<MessageItem> messageList = new ArrayList<>();
            messageList.add(new MessageItem("Flower Garden Shop", "Chào bạn, đơn hàng của bạn đã được gửi đi!", "10:30", R.drawable.hinh));
            messageList.add(new MessageItem("Tiệm Hoa Tươi", "Hoa hồng đỏ hiện đang có sẵn bạn nhé.", "09:15", R.drawable.hinh2));
            messageList.add(new MessageItem("Shop Hoa Đà Lạt", "Cảm ơn bạn đã ủng hộ shop!", "Hôm qua", R.drawable.hinh3));

            MessageAdapter adapter = new MessageAdapter(messageList, item -> {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                intent.putExtra("shopName", item.getShopName());
                startActivity(intent);
            });
            rcvMessages.setLayoutManager(new LinearLayoutManager(this));
            rcvMessages.setAdapter(adapter);
        }

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.getMenu().setGroupCheckable(0, false, true); 
            
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.navigation_home) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_product) {
                        startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_notifications) {
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_profile) {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}

class MessageItem {
    private String shopName;
    private String lastMessage;
    private String time;
    private int imageRes;

    public MessageItem(String shopName, String lastMessage, String time, int imageRes) {
        this.shopName = shopName;
        this.lastMessage = lastMessage;
        this.time = time;
        this.imageRes = imageRes;
    }

    public String getShopName() { return shopName; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
    public int getImageRes() { return imageRes; }
}

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageItem> messageList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MessageItem item);
    }

    public MessageAdapter(List<MessageItem> messageList, OnItemClickListener listener) {
        this.messageList = messageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem item = messageList.get(position);
        holder.tvShopName.setText(item.getShopName());
        holder.tvLastMessage.setText(item.getLastMessage());
        holder.tvTime.setText(item.getTime());
        holder.imgShopLogo.setImageResource(item.getImageRes());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return messageList.size(); }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView imgShopLogo;
        android.widget.TextView tvShopName, tvLastMessage, tvTime;

        public MessageViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            imgShopLogo = itemView.findViewById(R.id.imgShopLogo);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
