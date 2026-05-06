package com.example.hitcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderId());
        holder.tvOrderDate.setText(order.getDate());
        holder.tvOrderTotal.setText(formatPrice(order.getTotalAmount()));
        holder.tvOrderStatus.setText(getStatusText(order.getStatus()));

        // Clear and add items to the nested layout
        holder.lnOrderItems.removeAllViews();
        for (CartItem item : order.getItems()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_order_product, holder.lnOrderItems, false);
            
            ImageView img = itemView.findViewById(R.id.imgProductOrder);
            TextView name = itemView.findViewById(R.id.tvProductNameOrder);
            TextView qty = itemView.findViewById(R.id.tvProductQuantityOrder);
            TextView price = itemView.findViewById(R.id.tvProductPriceOrder);

            // Sửa lỗi: Sử dụng Glide để tải ảnh từ URL thay vì Resource ID
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.hinh)
                    .into(img);

            name.setText(item.getName());
            qty.setText("x" + item.getQuantity());
            price.setText(item.getPrice());

            holder.lnOrderItems.addView(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getStatusText(String status) {
        switch (status) {
            case "WaitConfirm": return "Chờ xác nhận";
            case "WaitPick": return "Chờ lấy hàng";
            case "Delivering": return "Đang giao";
            case "Delivered": return "Đã giao";
            case "Cancelled": return "Đã hủy";
            case "Returned": return "Trả hàng";
            default: return status;
        }
    }

    private String formatPrice(long price) {
        return String.format("%,d", price).replace(',', '.') + "đ";
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvOrderDate, tvOrderTotal;
        LinearLayout lnOrderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            lnOrderItems = itemView.findViewById(R.id.lnOrderItems);
        }
    }
}
