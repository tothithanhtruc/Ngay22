package com.example.hitcapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        
        // Dịch tên danh mục sang tiếng Việt
        holder.tvCategoryName.setText(translateCategory(category));

        if (selectedPosition == position) {
            holder.tvCategoryName.setBackgroundResource(R.drawable.bg_category_selected);
            holder.tvCategoryName.setTextColor(Color.WHITE);
        } else {
            holder.tvCategoryName.setBackgroundResource(R.drawable.bg_category_unselected);
            holder.tvCategoryName.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    // Hàm hỗ trợ dịch danh mục
    private String translateCategory(String category) {
        switch (category.toLowerCase()) {
            case "all": return "Tất cả";
            case "electronics": return "Điện tử";
            case "jewelery": return "Trang sức";
            case "men's clothing": return "Thời trang Nam";
            case "women's clothing": return "Thời trang Nữ";
            default: return category;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
