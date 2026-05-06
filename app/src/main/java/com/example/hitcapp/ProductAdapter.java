package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setFilteredList(List<Product> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_recommend, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getTitle());
        
        // Chuyển đổi giá sang VNĐ (giả định 1$ = 25.000đ)
        double priceVnd = product.getPrice() * 25000;
        String formattedPrice = String.format(Locale.GERMANY, "%,.0f đ", priceVnd);
        holder.tvPrice.setText(formattedPrice);

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.hinh)
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productName", product.getTitle());
            intent.putExtra("productPrice", formattedPrice);
            intent.putExtra("productImage", product.getImage());
            intent.putExtra("productDescription", product.getDescription()); // Thêm mô tả
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductThumb);
            tvName = itemView.findViewById(R.id.tvProductNameThumb);
            tvPrice = itemView.findViewById(R.id.tvProductPriceThumb);
        }
    }
}