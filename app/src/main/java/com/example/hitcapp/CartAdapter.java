package com.example.hitcapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onAmountChanged();
    }

    public CartAdapter(List<CartItem> cartItemList, OnCartChangeListener listener) {
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
        
        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.hinh)
                .into(holder.imgProduct);

        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        
        // Trạng thái CheckBox
        holder.cbItem.setOnCheckedChangeListener(null);
        holder.cbItem.setChecked(item.isSelected());

        holder.cbItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            if (listener != null) listener.onAmountChanged();
        });

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (listener != null) listener.onAmountChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                if (listener != null) listener.onAmountChanged();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            if (listener != null) listener.onAmountChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnPlus, btnMinus, btnDelete;
        TextView tvName, tvPrice, tvQuantity;
        CheckBox cbItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductCart);
            tvName = itemView.findViewById(R.id.tvProductNameCart);
            tvPrice = itemView.findViewById(R.id.tvProductPriceCart);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cbItem = itemView.findViewById(R.id.cbItem);
        }
    }
}
