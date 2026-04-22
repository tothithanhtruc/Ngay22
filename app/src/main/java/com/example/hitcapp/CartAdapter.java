package com.example.hitcapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
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
        holder.imgProduct.setImageResource(item.getImageId());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnPlus, btnMinus, btnDelete;
        TextView tvName, tvPrice, tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductCart);
            tvName = itemView.findViewById(R.id.tvProductNameCart);
            tvPrice = itemView.findViewById(R.id.tvProductPriceCart);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
