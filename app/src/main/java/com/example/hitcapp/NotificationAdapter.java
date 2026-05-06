package com.example.hitcapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.tvTitle.setText(notification.getTitle());
        holder.tvContent.setText(notification.getContent());

        // Set icon based on type
        if ("promo".equals(notification.getType())) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_send);
            holder.imgIcon.setColorFilter(0xFFE91E63);
        } else if ("order".equals(notification.getType())) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_agenda);
            holder.imgIcon.setColorFilter(0xFF4CAF50);
        } else {
            holder.imgIcon.setImageResource(android.R.drawable.ic_dialog_info);
            holder.imgIcon.setColorFilter(0xFF2196F3);
        }

        // Truyền cả object Notification sang trang chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationDetailActivity.class);
            intent.putExtra("notification", notification);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        ImageView imgIcon;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvContent = itemView.findViewById(R.id.tvNotificationContent);
            imgIcon = itemView.findViewById(R.id.imgNotificationIcon);
        }
    }
}